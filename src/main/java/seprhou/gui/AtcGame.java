package seprhou.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

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
	public Screen menuScreen, gameScreen, optionsScreen, scoresScreen, gameOverScreen;

	@Override
	public void create()
	{
		// Set initial screen
		showMenu();
	}

	/** Show the menu screen */
	public void showMenu()
	{
		if (menuScreen == null)
			menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

	/** Show the game screen */
	public void showGame()
	{
		if (gameScreen == null)
			gameScreen = new GameScreen(this);
		setScreen(gameScreen);
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
	
	/** Show the game over screen */
	public void showGameOver()
	{
		if (gameOverScreen == null)
			gameOverScreen = new GameOverScreen(this);
		setScreen(gameOverScreen);
	}
}
