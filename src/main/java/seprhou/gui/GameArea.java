package seprhou.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;

import com.badlogic.gdx.scenes.scene2d.Actor;
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

	/**
	 * Creates a new GameArea
	 */
	public GameArea(GameScreen parent)
	{
		this.parent = parent;
	}

	@Override
	public void act(float delta)
	{
		Airspace airspace = this.parent.getAirspace();

		// Die if the game is over
		// TODO Do we really want to do this?
		if (airspace.isGameOver())
			return;

		// TODO Handle input events

		// Refresh airspace
		airspace.refresh(delta);
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
				batch.draw(Assets.WAYPOINT_TEXTURE, point.getX(),point.getY());
			}
			

			// Draw all aircraft
			airspace.draw(batch);

			// TODO Draw collision warnings

			// End clipping
			clipEnd();
		}
	}
}
