package seprhou.gui;

import java.util.List;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import seprhou.logic.*;

/**
 * Class which manages the entire game area
 * <p>
 * Each game screen has one of these.
 */
public class GameArea extends Actor
{
	private final GameScreen parent;

	private SpriteBatch batch;

	/** Position of click event - null if there has been no clicks since last call to act */
	private Vector2D clickPosition;

	/** If true, a keydown event was received for the up / down buttons */
	private boolean upPressed, downPressed;

	/**
	 * Creates a new GameArea
	 */
	public GameArea(GameScreen parent)
	{
		this.parent = parent;
		this.addListener(new InputListener()
		{
			public boolean keyDown(InputEvent event, int keycode)
			{
				if (keycode == Input.Keys.UP)
				{
					upPressed = true;
					return true;
				}
				else if (keycode == Input.Keys.DOWN)
				{
					downPressed = true;
					return true;
				}

				return false;
			}

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
	public SpriteBatch getBatch()
	{
		return batch;
	}

	@Override
	public void act(float delta)
	{
		Airspace airspace = this.parent.getAirspace();

		// Die if the game is over
		// TODO Do we really want to do this?
		if (airspace.isGameOver()){
			parent.getGame().showGameOver(parent.getSecondsSinceStart());
			return;
		}
		// Selecting new aircraft
		if (clickPosition != null)
		{
			// Someone clicked on something - update selected aircraft
			parent.setSelectedAircraft((Aircraft) airspace.findAircraft(clickPosition));
			clickPosition = null;
		}

		// Keyboard controls
		Aircraft selected = parent.getSelectedAircraft();
		if (selected != null)
		{
			// TODO This is currently hooked up to the max turn rate - is this what we want?

			// These keys are updated each frame so they're checked here
			if (Gdx.input.isKeyPressed(Input.Keys.LEFT))
			{
				selected.setTargetVelocity(
						selected.getTargetVelocity()
								.rotate(selected.getMaxTurnRate() * delta));
			}
			else if (Gdx.input.isKeyPressed(Input.Keys.RIGHT))
			{
				// Note the rotation angle is NEGATIVE here
				selected.setTargetVelocity(
						selected.getTargetVelocity()
								.rotate(-selected.getMaxTurnRate() * delta));
			}

			// These keys are updated once - the Stage events handler works out when the down event occured
			if (upPressed)
				selected.setTargetAltitude(selected.getTargetAltitude() + Constants.ALTITUDE_JUMP);
			else if (downPressed)
				selected.setTargetAltitude(selected.getTargetAltitude() - Constants.ALTITUDE_JUMP);
		}

		// Clear keypress events
		upPressed = false;
		downPressed = false;

		// Refresh airspace
		airspace.refresh(delta);

		// Deselect the aircraft if it was culled
		if (airspace.getCulledObjects().contains(parent.getSelectedAircraft()))
			parent.setSelectedAircraft(null);
	}

	@Override
	public void draw(SpriteBatch batch, float parentAlpha)
	{
		Airspace airspace = this.parent.getAirspace();

		// Begin the clipping
		//  Note that batch.flush() MUST be called before calls to clipBegin() and clipEnd() because
		//  otherwise the opengl commands do not occur in the right order

		batch.flush();
		if (clipBegin())
		{
			// Draw all waypoints
			int waypointOffset = Assets.WAYPOINT_TEXTURE.getWidth() / 2;
			
			// Show next waypoint
			Aircraft selected = parent.getSelectedAircraft();
			Vector2D nextWayPoint = null;
			if (selected != null)
			{
				List<Vector2D> waypoints = selected.getFlightPlan().getWaypoints();
				int lastWayPoint = selected.getLastWaypoint();
				if (lastWayPoint < waypoints.size())
					nextWayPoint = waypoints.get(lastWayPoint+1);
			}
			
			for (Vector2D point : Constants.WAYPOINTS)
			{
				Texture waypointTexture;
				if (point == nextWayPoint)
					waypointTexture = Assets.NEXT_WAYPOINT_TEXTURE;
				else
					waypointTexture = Assets.WAYPOINT_TEXTURE;
				
				batch.draw(waypointTexture,
						getX() + point.getX() - waypointOffset,
						getY() + point.getY() - waypointOffset);
			}
			
			
			// Draw all aircraft
			this.batch = batch;
			airspace.draw(this);
			this.batch = null;

			// TODO Draw collision warnings
			for (CollisionWarning collision: airspace.getCollisionWarnings()){
				Vector2D position = collision.getObject1().getPosition();
				Vector2D position2 = collision.getObject2().getPosition();
				float circleRadius = Assets.CIRCLE_TEXTURE.getWidth() / 2;
				batch.draw(Assets.CIRCLE_TEXTURE, getX() + position.getX() - circleRadius, getY() + position.getY() - circleRadius);
				batch.draw(Assets.CIRCLE_TEXTURE, getX() + position2.getX() - circleRadius, getY() + position2.getY() - circleRadius);
			}
			
			
			// End clipping
			batch.flush();
			clipEnd();
		}
	}
}
