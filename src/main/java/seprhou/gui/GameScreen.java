package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import seprhou.logic.*;

/**
 * The main game screen
 *
 * <p>
 * The GameScreen consists of the GameArea (on the left) and the ControlPanel (on the right).
 * These classes control drawing of the game itself and the GameArea controls updates due to
 * interactions (clicking and key presses). The Airspace itself and the current game state is
 * controlled from this class however.
 */
public class GameScreen extends AbstractScreen
{
	public static final FlightPlanGenerator flightPathGenerator = new FlightPlanGenerator();

	private final GameArea gameArea;
	private final ControlPanel controlPanel;

	private Airspace airspace;
	private Aircraft selectedAircraft;
	private float secondsSinceStart;

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
		airspace.setLateralSeparation(OptionsScreen.getLateral());
		airspace.setVerticalSeparation(OptionsScreen.getVertical());
		airspace.setObjectFactory(new AirspaceObjectFactory()
		{
			@Override
			public AirspaceObject makeObject(Airspace airspace, float delta)
			{
				FlightPlan flightPlan = flightPathGenerator.makeFlightPlan(airspace, delta);

				if (flightPlan != null)
				{
					// Random flight number between YO000 and YO999
					String flightNumber = String.format("YO%03d", Utils.getRandom().nextInt(1000));

					return new ConcreteAircraft(flightNumber, 100, 5, flightPlan, airspace);
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

	/**
	 * Shorthand method to retrieve score
	 * 
	 * @return current score
	 */
	public int getScore() {
		return this.getAirspace().getScore();
	}

}
