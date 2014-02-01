package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import seprhou.logic.Aircraft;
import seprhou.logic.Airspace;
import seprhou.logic.AirspaceObject;
import seprhou.logic.AirspaceObjectFactory;
import seprhou.logic.FlightPlan;
import seprhou.logic.FlightPlanGenerator;
import seprhou.logic.Rectangle;
import seprhou.logic.Utils;

/**
 * The main game screen
 * 
 * <p>
 * The GameScreen consists of the GameArea (on the left) and the ControlPanel
 * (on the right). These classes control drawing of the game itself and the
 * GameArea controls updates due to interactions (clicking and key presses). The
 * Airspace itself and the current game state is controlled from this class
 * however.
 */
public class GameScreen extends AbstractScreen {
	private static final FlightPlanGenerator flightPathGenerator;

	private final GameArea gameArea;
	private final ControlPanel controlPanel;

	private Airspace airspace;
	private Aircraft selectedAircraft;
	private float secondsSinceStart;

	static {
		flightPathGenerator = new FlightPlanGenerator();
		GameScreen.flightPathGenerator.setWaypoints(Constants.WAYPOINTS);
		GameScreen.flightPathGenerator.setEntryExitPoints(Constants.ENTRY_EXIT_POINTS);
		GameScreen.flightPathGenerator.setInitialAltitudes(Constants.INITIAL_ALTITUDES);
		GameScreen.flightPathGenerator.setInitialSpeeds(Constants.INITIAL_SPEEDS);
		GameScreen.flightPathGenerator.setMinSafeEntryDistance(Constants.MIN_SAFE_ENTRY_DISTANCE);
		GameScreen.flightPathGenerator.setMinTimeBetweenAircraft(Constants.MIN_TIME_BETWEEN_AIRCRAFT);
		GameScreen.flightPathGenerator.setAircraftPerSec(Constants.AIRCRAFT_PER_SEC);
		GameScreen.flightPathGenerator.setMaxAircraft(Constants.MAX_AIRCRAFT);
		GameScreen.flightPathGenerator.setMinWaypoints(Constants.MIN_WAYPOINTS);
		GameScreen.flightPathGenerator.setMaxWaypoints(Constants.MAX_WAYPOINTS);
	}

	public GameScreen(AtcGame game) {
		super(game);
		Stage stage = this.getStage();

		// Background image
		Image background = new Image(Assets.BACKGROUND_TEXTURE);
		stage.addActor(background);

		// Create the game area (where the action takes place)
		this.gameArea = new GameArea(this);
		this.gameArea.setBounds(0, 0, 1400, AbstractScreen.SCREEN_HEIGHT);
		stage.addActor(this.gameArea);

		// Create the control panel (where info is displayed)
		this.controlPanel = new ControlPanel(this);
		this.controlPanel.setBounds(1400, 0, 280, AbstractScreen.SCREEN_HEIGHT);
		stage.addActor(this.controlPanel);

		// Add timer update actor
		stage.addActor(new Actor() {
			@Override
			public void act(float delta) {
				GameScreen.this.secondsSinceStart += delta;
			}
		});

		// Give the game area keyboard focus (this is where keyboard events are
		// sent)
		stage.setKeyboardFocus(this.gameArea);
	}

	@Override
	public void show() {
		super.show();

		// Create the airspace
		this.airspace = new Airspace();
		this.airspace.setDimensions(new Rectangle(this.gameArea.getWidth(), this.gameArea.getHeight()));
		this.airspace.setLateralSeparation(Constants.LATERAL_SEPARATION);
		this.airspace.setVerticalSeparation(Constants.VERTICAL_SEPARATION);
		this.airspace.setObjectFactory(new AirspaceObjectFactory() {
			@Override
			public AirspaceObject makeObject(Airspace airspace, float delta) {
				FlightPlan flightPlan = GameScreen.flightPathGenerator.makeFlightPlan(airspace, delta);

				if (flightPlan != null) {
					// Random flight number between YO000 and YO999
					String flightNumber = String.format("YO%03d", Utils.getRandom().nextInt(1000));

					return new ConcreteAircraft(flightNumber, 100, 5, flightPlan);
				}

				return null;
			}
		});

		// Reset information from past games
		this.selectedAircraft = null;
		this.secondsSinceStart = 0;
	}

	/** Returns the airspace object used by the game */
	public Airspace getAirspace() {
		return this.airspace;
	}

	/**
	 * Returns the currently selected aircraft or null if nothing is selected
	 */
	public Aircraft getSelectedAircraft() {
		return this.selectedAircraft;
	}

	/**
	 * Returns the number of seconds since the start of this game
	 */
	public float getSecondsSinceStart() {
		return this.secondsSinceStart;
	}

	/**
	 * Sets the currently selected aircraft
	 * 
	 * @param selectedAircraft new current aircraft or null for no selected
	 *            aircraft
	 */
	public void setSelectedAircraft(Aircraft selectedAircraft) {
		this.selectedAircraft = selectedAircraft;
	}
}
