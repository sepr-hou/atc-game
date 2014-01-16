package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import seprhou.logic.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameScreen extends AbstractScreen
{
	// Game parameters
	private static final float LATERAL_SEPARATION = 200.0f;
	private static final float VERTICAL_SEPARATION = 200.0f;
	static final List<Vector2D> WAYPOINTS = new ArrayList<>(Arrays.asList(
			new Vector2D(280, 210),
			new Vector2D(280, 420),
			new Vector2D(280, 630),
			new Vector2D(280, 840),
			new Vector2D(560, 210),
			new Vector2D(560, 420),
			new Vector2D(560, 630),
			new Vector2D(560, 840),
			new Vector2D(840, 210),
			new Vector2D(840, 420),
			new Vector2D(840, 630),
			new Vector2D(840, 840),
			new Vector2D(1120, 210),
			new Vector2D(1120, 420),
			new Vector2D(1120, 630),
			new Vector2D(1120, 840)
	));

	private static final List<Vector2D> ENTRY_EXIT_POINTS = new ArrayList<>(Arrays.asList(
			new Vector2D(100, 0),
			new Vector2D(0, 800)
	));

	private final GameArea gameArea;
	private final ControlPanel controlPanel;

	private Airspace airspace;
	private Aircraft selectedAircraft;

	public GameScreen(AtcGame game)
	{
		super(game);
		Stage stage = getStage();

		// Create the game area (where the action takes place)
		gameArea = new GameArea(this);
		gameArea.setBounds(0, 0, 1400, SCREEN_HEIGHT);
		stage.addActor(gameArea);

		// Create the control panel (where info is displayed)
		controlPanel = new ControlPanel(this);
		controlPanel.setBounds(1400, 0, 280, SCREEN_HEIGHT);
		stage.addActor(controlPanel);
				
		// Give the game area keyboard focus (this is where keyboard events are sent)
		stage.setKeyboardFocus(gameArea);
	}

	@Override
	public void show()
	{
		super.show();

		// Create the airspace
		airspace = new Airspace();
		airspace.setObjectFactory(new GameAircraftFactory());
		airspace.setDimensions(new Rectangle(gameArea.getWidth(), gameArea.getHeight()));
		airspace.setLateralSeparation(LATERAL_SEPARATION);
		airspace.setVerticalSeparation(VERTICAL_SEPARATION);
		selectedAircraft = null;
	}

	/** Returns the airspace object used by the game */
	public Airspace getAirspace()
	{
		return airspace;
	}

	/**
	 * Returns the currently selected aircraft or null if nothing is selected
	 */
	public Aircraft getSelectedAircraft()
	{
		return selectedAircraft;
	}

	/**
	 * Sets the currently selected aircraft
	 *
	 * @param selectedAircraft new current aircraft or null for no selected aircraft
	 */
	public void setSelectedAircraft(Aircraft selectedAircraft)
	{
		this.selectedAircraft = selectedAircraft;
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
