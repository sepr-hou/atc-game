package seprhou.gui;

import com.badlogic.gdx.graphics.Texture;
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

	private final AircraftObjectFactory AIRCRAFT_FACTORY = new GameAircraftFactory();
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
		if (clipBegin())
		{
			// Draw background
			batch.draw(Assets.BACKGROUND_TEXTURE, getX(), getY());

			// Draw all aircraft
			airspace.draw(batch);

			// TODO Draw collision warnings

			// End clipping
			clipEnd();
		}
	}

	private class GameAircraftFactory implements AircraftObjectFactory
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
				//List<Waypoint> flightPath = flightPathGenerator.makeFlightPath(airspace);

				// TODO Use proper flight path generator
				List<Waypoint> flightPath = new ArrayList<>();
				flightPath.add(new Waypoint(new Vector2D(0, 0), 20, 20));
				flightPath.add(new Waypoint(new Vector2D(3, 3), 20, 20));

				// TODO Possibly adjust / randomize these arguments
				return new ConcreteAircraft("The Destroyer", 100000.0f, 5, flightPath);
			}

			return null;
		}
	}

	/** The only type of aircraft (currently?) available */
	private class ConcreteAircraft extends Aircraft
	{
		public ConcreteAircraft(String name, float weight, int crew, List<Waypoint> flightPlan)
		{
			super(name, weight, crew, flightPlan);
		}

		@Override
		public void draw(Object state)
		{
			SpriteBatch batch = (SpriteBatch) state;
			float angleDegrees = getVelocity().getAngle() * (float) (180.0 / Math.PI);

			// Add parent X and Y since SpriteBatch does not adjust coordinates for the Actor
			float xPos = GameArea.this.getX() + getPosition().getX() - getSize();
			float yPos = GameArea.this.getY() + getPosition().getY() - getSize();

			batch.draw(
				Assets.AIRCRAFT_TEXTURE,            // Aircraft texture
				xPos,                               // X position (bottom left)
				yPos,                               // Y position (bottom right)
				getSize(),                          // X rotation origin
				getSize(),                          // Y rotation origin
				getSize() * 2,                      // Width
				getSize() * 2,                      // Height
				1.0f,                               // X scaling
				1.0f,                               // Y scaling
				angleDegrees,                       // Rotation
				0,                                  // X position in texture
				0,                                  // Y position in texture
				Assets.AIRCRAFT_TEXTURE.getWidth(), // Width of source texture
				Assets.AIRCRAFT_TEXTURE.getHeight(),// Height of source texture
				false,                              // Flip in X axis
				false                               // Flip in Y axis
			);
		}

		@Override public float getSize()                { return 64; }
		@Override protected float getAscentRate()       { return 10; }
		@Override protected float getMinSpeed()         { return 0; }
		@Override protected float getMaxSpeed()         { return 100; }
		@Override protected float getMaxAltitude()      { return 100; }
		@Override protected float getMaxAcceleration()  { return 10; }
		@Override protected float getMaxTurnRate()      { return 10; }
	}
}
