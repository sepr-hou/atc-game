package seprhou.gui;

import seprhou.logic.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameScreen extends AbstractScreen
{
	// Game parameters
	private static final float HORIZONTAL_SEPARATION = 10.0f;
	private static final float VERTICAL_SEPARATION = 10.0f;
	private static final List<Vector2D> WAYPOINTS = new ArrayList<>(Arrays.asList(
			new Vector2D(50, 0),
			new Vector2D(0, 50),
			new Vector2D(200, 0),
			new Vector2D(30, 0)
	));
	private static final List<Vector2D> ENTRY_EXIT_POINTS = new ArrayList<>(Arrays.asList(
			new Vector2D(100, 0),
			new Vector2D(0, 100)
	));

	private final AircraftObjectFactory AIRCRAFT_FACTORY = new GameAircraftFactory();

	private Airspace airspace;
	private GameArea gameArea;


	public GameScreen(AtcGame game)
	{
		super(game);

		// Create the game area (where the action takes place)
		gameArea = new GameArea(this);
		gameArea.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		defaultStage.addActor(gameArea);

		// Create the airspace
		Rectangle dimensions = new Rectangle(gameArea.getWidth(), gameArea.getHeight());
		AirspaceConfig config = new AirspaceConfig(AIRCRAFT_FACTORY, dimensions,
				HORIZONTAL_SEPARATION, VERTICAL_SEPARATION);

		airspace = new Airspace(config);
	}

	public Airspace getAirspace()
	{
		return airspace;
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
				List<Waypoint> flightPath = flightPathGenerator.makeFlightPath(airspace);

				// TODO Possibly adjust / randomize these arguments
				return new ConcreteAircraft("The Destroyer", 100000.0f, 5, flightPath);
			}

			return null;
		}
	}
}
