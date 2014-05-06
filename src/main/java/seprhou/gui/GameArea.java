package seprhou.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import seprhou.logic.*;
import seprhou.network.GameEndpoint;
import seprhou.network.GameEndpointState;

import java.util.BitSet;
import java.util.List;

/**
 * Class which manages the entire game area
 * <p>
 * Each game screen has one of these.
 */
public class GameArea extends Actor
{
	private final GameScreen parent;

	private Batch batch;

	/** Position of click event - null if there has been no clicks since last call to act */
	private Vector2D clickPosition;

	/** Set containing the keys pressed since the last act */
	private BitSet buttonsPressed = new BitSet();

	/**
	 * Creates a new GameArea
	 */
	public GameArea(GameScreen parent)
	{
		this.parent = parent;
		this.addListener(new InputListener()
		{
			@Override
			public boolean keyDown(InputEvent event, int keycode)
			{
				buttonsPressed.set(keycode);
				return true;
			}

			@Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button)
			{
				if (button == Input.Buttons.LEFT)
				{
					// Store click position to be processed later in the act event
					clickPosition = new Vector2D(x, y);
					return true;
				}

				return false;
			}
		});
	}

	/**
	 * Returns the game screen used by this actor
	 */
	public GameScreen getGameScreen()
	{
		return parent;
	}

	/**
	 * Returns the batch used for drawing
	 *
	 * <p>This is only defined when within the draw method
	 */
	public Batch getBatch()
	{
		return batch;
	}

	@Override
	public void act(float delta)
	{
		GameEndpoint endpoint = parent.getEndpoint();
		Airspace airspace = endpoint.getAirspace();

		// Process received network messages
		endpoint.actBegin();

		// Test for game over
		if (airspace.isGameOver())
		{
			parent.getGame().showGameOver(parent.getSecondsSinceStart(), parent.getScore());
			return;
		}

		// Test for network outage
		if (endpoint.getState() != GameEndpointState.CONNECTED)
		{
			// TODO Handle network failure a bit better than this!
			throw new RuntimeException("Network failiure!", endpoint.getFailException());
		}

		// Get currently selected aircraft
		Aircraft selected = parent.getSelectedAircraft();

		// Deselect the aircraft if it was culled
		if (airspace.getCulledObjects().contains(selected))
			selected = null;

		// Selecting new aircraft
		if (clickPosition != null)
		{
			if (LogicConstants.DEBUG)
				System.out.println(this.clickPosition);

			// Mouse click registered - update selected aircraft (if it's the right colour)
			selected = null;

			Aircraft aircraft = (Aircraft) airspace.findAircraft(clickPosition);
			if (aircraft != null && aircraft.getColour() == endpoint.getMyColour())
				selected = aircraft;

			clickPosition = null;
		}

		// Tab Cycling through aircraft
		if (buttonsPressed.get(Input.Keys.TAB))
			selected = (Aircraft) airspace.cycleAircraft();

		// Deselect aircraft if we don't control it
		if (selected != null && selected.getColour() != endpoint.getMyColour())
			selected = null;

		// Keyboard controls
		if (selected != null && selected.isActive())
		{
			Vector2D oldTargetVelocity = selected.getTargetVelocity();
			float oldTargetAltitude = selected.getTargetAltitude();

			// These keys are updated each frame so they're checked here
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A))
			{
				endpoint.setTargetVelocity(selected, oldTargetVelocity.rotate(selected.getMaxTurnRate() * delta));
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D))
			{
				// Note the rotation angle is NEGATIVE here
				endpoint.setTargetVelocity(selected, oldTargetVelocity.rotate(-selected.getMaxTurnRate() * delta));
			}

			if (buttonsPressed.get(Input.Keys.Q))
			{
				// Slow down by 100mph
				endpoint.setTargetVelocity(selected, oldTargetVelocity.changeLength(oldTargetVelocity.getLength() - 10));
			}
			else if (buttonsPressed.get(Input.Keys.E))
			{
				// Speed up by 100mph
				endpoint.setTargetVelocity(selected, oldTargetVelocity.changeLength(oldTargetVelocity.getLength() + 10));
			}

			// These keys are updated once - the Stage events handler works out when the down event occured
			if (buttonsPressed.get(Input.Keys.UP))
				endpoint.setTargetAltitude(selected, oldTargetAltitude + LogicConstants.ALTITUDE_JUMP);
			else if (buttonsPressed.get(Input.Keys.DOWN))
				endpoint.setTargetAltitude(selected, oldTargetAltitude - LogicConstants.ALTITUDE_JUMP);

			if (buttonsPressed.get(Input.Keys.H))
				endpoint.handover(selected);
		}

		// Save selected aircraft
		parent.setSelectedAircraft(selected);

		// Takes off landed airplanes.
		// Additional check, to make sure, that it is impossible to have more
		// than MAX_AIRCRAFT planes in the airspace.
		if (buttonsPressed.get(Input.Keys.SPACE) && airspace.getActiveObjects().size() < LogicConstants.MAX_AIRCRAFT)
			endpoint.takeOff();

		// Clear keypress events
		buttonsPressed.clear();

		// Refresh airspace + process sent network messages
		endpoint.actEnd(delta);
	}

	@Override
	public void draw(Batch batch, float parentAlpha)
	{
		Airspace airspace = this.parent.getAirspace();

		// Begin the clipping
		//  Note that batch.flush() MUST be called before calls to clipBegin() and clipEnd() because
		//  otherwise the opengl commands do not occur in the right order

		batch.flush();
		if (clipBegin())
		{
			Aircraft selected = parent.getSelectedAircraft();

			// Draw all waypoints
			int waypointOffset = Assets.WAYPOINT_TEXTURE.getWidth() / 2;

			for (Vector2D point : LogicConstants.WAYPOINTS)
			{
				batch.draw(Assets.WAYPOINT_TEXTURE,
						getX() + point.getX() - waypointOffset,
						getY() + point.getY() - waypointOffset);
			}

			// Draw flight path + highlighted waypoints
			if (selected != null)
			{
				List<Vector2D> waypoints = selected.getFlightPlan().getWaypoints();
				Vector2D current = selected.getPosition();

				// Draw from current position to next waypoint, to next waypoint (etc)
				for (int i = selected.getLastWaypoint() + 1; i < waypoints.size(); i++)
				{
					Vector2D waypoint = waypoints.get(i);

					if (i == waypoints.size() - 1)
					{
						// Draw exit point
						batch.draw(Assets.CIRCLE_TEXTURE,
								getX() + waypoint.getX() - Assets.CIRCLE_TEXTURE.getWidth() / 2,
								getY() + waypoint.getY() - Assets.CIRCLE_TEXTURE.getHeight() / 2);
					}
					else
					{
						// Draw highlighted waypoint
						batch.draw(Assets.NEXT_WAYPOINT_TEXTURE,
								getX() + waypoint.getX() - waypointOffset,
								getY() + waypoint.getY() - waypointOffset);
					}

					// Draw line
					drawLine(batch, current, waypoint, Color.ORANGE, 2);
					current = waypoint;
				}
			}

			// Draw landed planes
			for (int i = 0; i < airspace.getLandedObjects(); i++)
			{
				Vector2D pos = LogicConstants.LANDED_AIRCRAFT_POSITIONS.get(i);
				float rot = LogicConstants.LANDED_AIRCRAFT_ANGLES.get(i);

				batch.draw(Assets.AIRCRAFT_TEXTURE,         // Aircraft texture
						pos.getX(),                         // X position (bottom left)
						pos.getY(),                         // Y position (bottom right)
						LogicConstants.AIRCRAFT_SIZE,       // X rotation origin
						LogicConstants.AIRCRAFT_SIZE,       // Y rotation origin
						LogicConstants.AIRCRAFT_SIZE * 2,   // Width
						LogicConstants.AIRCRAFT_SIZE * 2,   // Height
						0.8f,                               // X scaling
						0.8f,                               // Y scaling
						rot,                                // Rotation
						0,                                  // X position in texture
						0,                                  // Y position in texture
						Assets.AIRCRAFT_TEXTURE.getWidth(), // Width of source texture
						Assets.AIRCRAFT_TEXTURE.getHeight(),// Height of source texture
						false,                              // Flip in X axis
						false                               // Flip in Y axis
				);
			}

			// Draw all aircraft
			this.batch = batch;
			airspace.draw(this);
			this.batch = null;

			// Draw collision warnings
			for (CollisionWarning collision : airspace.getCollisionWarnings())
			{
				Vector2D position = collision.getObject1().getPosition();
				Vector2D position2 = collision.getObject2().getPosition();
				float circleRadius = Assets.VIOLATED_TEXTURE.getWidth() / 2;

				batch.draw(Assets.VIOLATED_TEXTURE,
						getX() + position.getX() - circleRadius,
						getY() + position.getY() - circleRadius);
				batch.draw(Assets.VIOLATED_TEXTURE,
						getX() + position2.getX() - circleRadius,
						getY() + position2.getY() - circleRadius);
			}
			
			// End clipping
			batch.flush();
			clipEnd();
		}
	}

	/**
	 * Draws a line using a Batch
	 *
	 * @param batch batch to draw to
	 * @param color line color
	 * @param a first point
	 * @param b second point
	 */
	private void drawLine(Batch batch, Vector2D a, Vector2D b, Color color, float thickness)
	{
		Vector2D vectorDiff = b.sub(a);
		float length = vectorDiff.getLength();
		float angle = (float) (vectorDiff.getAngle() * 180 / Math.PI);

		Color prevColor = batch.getColor();
		batch.setColor(color);

		batch.draw(
				Assets.BLANK,       // Aircraft texture
				getX() + a.getX(),  // X position (bottom left)
				getY() + a.getY(),  // Y position (bottom right)
				0,                  // X rotation origin
				0,                  // Y rotation origin
				length,             // Width
				thickness,          // Height
				1.0f,               // X scaling
				1.0f,               // Y scaling
				angle,              // Rotation
				0,                  // X position in texture
				0,                  // Y position in texture
				1,                  // Width of source texture
				1,                  // Height of source texture
				false,              // Flip in X axis
				false               // Flip in Y axis
		);

		batch.setColor(prevColor);
	}
}
