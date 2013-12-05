package seprhou.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import com.badlogic.gdx.scenes.scene2d.Actor;
import seprhou.logic.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class which manages the entire game area
 * <p>
 * Each game screen has one of these.
 */
public class GameArea extends Actor
{
	// Game parameters
	private static final float HORIZONTAL_SEPARATION = 10.0f;
	private static final float VERTICAL_SEPARATION = 10.0f;
	private static final List<Vector2D> WAYPOINTS = new ArrayList<>();
	private static final List<Vector2D> ENTRY_EXIT_POINTS = new ArrayList<>();

	private static final AircraftObjectFactory AIRCRAFT_FACTORY = new GameAircraftFactory();

	private Airspace airspace;

	/**
	 * Returns the airspace used by the GameArea
	 * <p>
	 * Until the first time the stage is acted, the airspace may be null.
	 */
	public Airspace getAirspace()
	{
		return airspace;
	}

	@Override
	public void act(float delta)
	{
		// Create airspace the first time we act
		if (airspace == null)
		{
			Rectangle dimensions = new Rectangle(getWidth(), getHeight());
			AirspaceConfig config = new AirspaceConfig(AIRCRAFT_FACTORY, dimensions,
					HORIZONTAL_SEPARATION, VERTICAL_SEPARATION);

			airspace = new Airspace(config);
		}

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
		batch.draw(Assets.backgroundTexture,0,0);
		// Draw all aircraft
		airspace.draw(batch);

		// TODO Draw collision warnings
	}

	private static class GameAircraftFactory implements AircraftObjectFactory
	{
		private DefaultFlightPathGenerator flightPathGenerator =
				new DefaultFlightPathGenerator(WAYPOINTS, ENTRY_EXIT_POINTS);
		private DefaultObjectCreationRate objectCreationRate =
				new DefaultObjectCreationRate();

		@Override
		public AirspaceObject makeObject(Airspace airspace, float delta)
		{
			if (objectCreationRate.nextBoolean(airspace, delta))
			{
				List<Waypoint> flightPath = flightPathGenerator.makeFlightPath(airspace);

				// TODO Create an instance of Aircraft and return it
				return null;
			}

			return null;
		}
	}
}
