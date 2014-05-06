package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import seprhou.logic.Aircraft;
import seprhou.logic.Airspace;
import seprhou.logic.Rectangle;
import seprhou.network.GameEndpoint;

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
	private static final int GAME_AREA_WIDTH = 1400;
	public static final Rectangle GAME_DIMENSIONS = new Rectangle(GAME_AREA_WIDTH, SCREEN_HEIGHT);

	private final GameArea gameArea;
	private final ControlPanel controlPanel;
	private final GameEndpoint endpoint;

	private Aircraft selectedAircraft;
	private float secondsSinceStart;

	public GameScreen(AtcGame game, GameEndpoint endpoint)
	{
		super(game);
		Stage stage = getStage();

		// Background image
		Image background = new Image(Assets.BACKGROUND_TEXTURE);
		stage.addActor(background);

		// Create the game area (where the action takes place)
		gameArea = new GameArea(this);
		gameArea.setBounds(0, 0, GAME_AREA_WIDTH, SCREEN_HEIGHT);
		stage.addActor(gameArea);

		// Set endpoint
		this.endpoint = endpoint;
		
		// Create the control panel (where info is displayed)
		controlPanel = new ControlPanel(this);
		controlPanel.setBounds(GAME_AREA_WIDTH, 0, SCREEN_WIDTH - GAME_AREA_WIDTH, SCREEN_HEIGHT);
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
	public void hide()
	{
		super.hide();

		// Close the endpoint when leaving this screen
		if (endpoint != null)
			endpoint.close();
	}

	/** Returns the network endpoint */
	public GameEndpoint getEndpoint() { return endpoint; }

	/** Returns the airspace object used by the game */
	public Airspace getAirspace()
	{
		return endpoint.getAirspace();
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
