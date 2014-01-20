package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import seprhou.logic.*;

public class GameScreen extends AbstractScreen
{
	private static final FlightPlanGenerator flightPathGenerator;

	private final GameArea gameArea;
	private final ControlPanel controlPanel;

	private Airspace airspace;
	private Aircraft selectedAircraft;
	private float secondsSinceStart;

	static
	{
		flightPathGenerator = new FlightPlanGenerator();
		flightPathGenerator.setWaypoints(Constants.WAYPOINTS);
		flightPathGenerator.setEntryExitPoints(Constants.ENTRY_EXIT_POINTS);
		flightPathGenerator.setInitialAltitudes(Constants.INITIAL_ALTITUDES);
		flightPathGenerator.setInitialSpeeds(Constants.INITIAL_SPEEDS);
		flightPathGenerator.setMinSafeEntryDistance(Constants.MIN_SAFE_ENTY_DISTANCE);
		flightPathGenerator.setMinTimeBetweenAircraft(Constants.MIN_TIME_BETWEEN_AIRCRAFT);
		flightPathGenerator.setAircraftPerSec(Constants.AIRCRAFT_PER_SEC);
		flightPathGenerator.setMaxAircraft(Constants.MAX_AIRCRAFT);
		flightPathGenerator.setMinWaypoints(Constants.MIN_WAYPOINTS);
		flightPathGenerator.setMaxWaypoints(Constants.MAX_WAYPOINTS);
	}

	public GameScreen(AtcGame game)
	{
		super(game);
		Stage stage = getStage();

		// Background image
		Image background = new Image(Assets.BACKGROUND_TEXTURE);
		stage.addActor(background);

		// Create the game area (where the action takes place)
		gameArea = new GameArea(this);
		gameArea.setBounds(0, 0, 1400, SCREEN_HEIGHT);
		stage.addActor(gameArea);

		// Create the control panel (where info is displayed)
		controlPanel = new ControlPanel(this);
		controlPanel.setBounds(1400, 0, 280, SCREEN_HEIGHT);
		stage.addActor(controlPanel);

		// Add timer update actor
		stage.addActor(new Actor()
		{
			@Override
			public void act(float delta)
			{
				secondsSinceStart += delta;
			}
		});
				
		// Give the game area keyboard focus (this is where keyboard events are sent)
		stage.setKeyboardFocus(gameArea);
	}

	@Override
	public void show()
	{
		super.show();

		// Create the airspace
		airspace = new Airspace();
		airspace.setDimensions(new Rectangle(gameArea.getWidth(), gameArea.getHeight()));
		airspace.setLateralSeparation(Constants.LATERAL_SEPARATION);
		airspace.setVerticalSeparation(Constants.VERTICAL_SEPARATION);
		airspace.setObjectFactory(new AircraftObjectFactory()
		{
			@Override
			public AirspaceObject makeObject(Airspace airspace, float delta)
			{
				FlightPlan flightPlan = flightPathGenerator.makeFlightPlan(airspace, delta);

				if (flightPlan != null)
				{
					// TODO Possibly adjust / randomize these arguments
					return new ConcreteAircraft("The Destroyer", 100000.0f, 5, flightPlan);
				}

				return null;
			}
		});

		// Reset information from past games
		selectedAircraft = null;
		secondsSinceStart = 0;
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
	 * Returns the number of seconds since the start of this game
	 */
	public float getSecondsSinceStart()
	{
		return secondsSinceStart;
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
}
