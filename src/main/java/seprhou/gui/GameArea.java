package seprhou.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
	/** Amount the altitude jumps to when a key is pressed */
	private static final float ALTITUDE_JUMP = 5000;

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
			parent.getGame().showGameOver();
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
				selected.setTargetAltitude(selected.getTargetAltitude() + ALTITUDE_JUMP);
			else if (downPressed)
				selected.setTargetAltitude(selected.getTargetAltitude() - ALTITUDE_JUMP);
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

		if (clipBegin())
		{
			// Draw background
			batch.draw(Assets.BACKGROUND_TEXTURE, getX(), getY());

			// Draw all waypoints
			int waypointOffset = Assets.WAYPOINT_TEXTURE.getWidth() / 2;

			for (Vector2D point: GameScreen.WAYPOINTS){
				batch.draw(Assets.WAYPOINT_TEXTURE,
						point.getX() - waypointOffset,
						point.getY() - waypointOffset);
			}

			// Draw all aircraft
			this.batch = batch;
			airspace.draw(this);
			this.batch = null;

			// Draw selected circle
			Aircraft selected = parent.getSelectedAircraft();
			if (selected != null)
			{
				Vector2D position = selected.getPosition();
				float circleRadius = Assets.CIRCLE_TEXTURE.getWidth() / 2;
				batch.draw(Assets.CIRCLE_TEXTURE, getX() + position.getX() - circleRadius, getY() + position.getY() - circleRadius);
			}

			// TODO Draw collision warnings
			for (CollisionWarning collision: airspace.getCollisionWarnings()){
				Vector2D position = collision.getObject1().getPosition();
				Vector2D position2 = collision.getObject2().getPosition();
				float circleRadius = Assets.CIRCLE_TEXTURE.getWidth() / 2;
				batch.draw(Assets.CIRCLE_TEXTURE, getX() + position.getX() - circleRadius, getY() + position.getY() - circleRadius);
				batch.draw(Assets.CIRCLE_TEXTURE, getX() + position2.getX() - circleRadius, getY() + position2.getY() - circleRadius);
			}
			// End clipping
			clipEnd();
		}
	}
}
