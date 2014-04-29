package seprhou.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;
import seprhou.highscores.HighscoresFile;
import seprhou.network.NetworkEndpoint;

/**
 * The main class of the game
 *
 * <p>
 * This class is used for all incoming events (like {@link com.badlogic.gdx.Game#render()}
 * from libGDX. After the game is setup, all the events received by this class
 * are simply passed to the current screen.
 */
public class AtcGame extends Game
{
	private HighscoresFile globalScores;

	private Screen menuScreen, optionsScreen, scoresScreen, netConfScreen;
	private GameOverScreen gameOverScreen;

	@Override
	public void create()
	{
		// Set initial screen
		showMenu();

		// Read highscores
		globalScores = HighscoresFile.readFromDefaultFile();
	}

	/**
	 * Returns the global highscores file
	 */
	public HighscoresFile getGlobalScores()
	{
		return globalScores;
	}

	/** Show the menu screen */
	public void showMenu()
	{
		if (menuScreen == null)
			menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	/**
	 * Show the game screen
	 *
	 * @param endpoint endpoint to use in the game
	 */
	public void showGame(NetworkEndpoint endpoint)
	{
		setScreen(new GameScreen(this, endpoint));
	}

	/** Show the options screen */
	public void showOptions()
	{
		if (optionsScreen == null)
			optionsScreen = new OptionsScreen(this);
		setScreen(optionsScreen);
	}

	/** Show the high scores screen */
	public void showHighScores()
	{
		if (scoresScreen == null)
			scoresScreen = new HighScoresScreen(this);
		setScreen(scoresScreen);
	}
	
	/** Show the network configuration screen */
	public void showNetworkConfig()
	{
		if  (netConfScreen == null)
			netConfScreen = new NetworkConfigScreen(this);
		setScreen(netConfScreen);
	}
	
	/**
	 * Show the game over screen
	 *
	 * @param time the number of seconds elapsed when the game finished
	 */
	public void showGameOver(float time, int score)
	{
		if (gameOverScreen == null)
			gameOverScreen = new GameOverScreen(this);

		gameOverScreen.setTimerValue(time, score);
		setScreen(this.gameOverScreen);
	}
}
