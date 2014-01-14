package seprhou.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import seprhou.logic.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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

	/**
	 * Creates a new GameArea
	 */
	public GameArea(GameScreen parent)
	{
		this.parent = parent;
		this.addListener(new InputListener()
		{
			public boolean touchDown (InputEvent event, float x, float y, int pointer, int button)
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
		if (airspace.isGameOver())
			return;

		// Handle input events
		if (clickPosition != null)
		{
			// Someone clicked on something - update selected aircraft
			parent.setSelectedAircraft((Aircraft) airspace.findAircraft(clickPosition));
			clickPosition = null;
		}

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
			for (Vector2D point: GameScreen.WAYPOINTS){
				batch.draw(Assets.WAYPOINT_TEXTURE, point.getX()-5,point.getY()-5);
			}
			

			// Draw all aircraft
			airspace.draw(batch);

			// TODO Draw collision warnings

			// End clipping
			clipEnd();
		}
	}
}
