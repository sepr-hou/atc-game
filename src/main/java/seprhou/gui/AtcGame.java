package seprhou.gui;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Screen;

/**
 * The main class of the game
 * 
 * <p>
 * This class is used for all incoming events (like
 * {@link com.badlogic.gdx.Game#render()} from libGDX. After the game is setup,
 * all the events received by this class are simply passed to the current
 * screen.
 */
public class AtcGame extends Game {
	private Screen menuScreen, gameScreen, optionsScreen, scoresScreen;
	private GameOverScreen gameOverScreen;

	@Override
	public void create() {
		// Set initial screen
		this.showMenu();
	}

	/** Show the menu screen */
	public void showMenu() {
		if (this.menuScreen == null) {
			this.menuScreen = new MenuScreen(this);
		}
		this.setScreen(this.menuScreen);
	}

	/** Show the game screen */
	public void showGame() {
		if (this.gameScreen == null) {
			this.gameScreen = new GameScreen(this);
		}
		this.setScreen(this.gameScreen);
	}

	/** Show the options screen */
	public void showOptions() {
		if (this.optionsScreen == null) {
			this.optionsScreen = new OptionsScreen(this);
		}
		this.setScreen(this.optionsScreen);
	}

	/** Show the high scores screen */
	public void showHighScores() {
		if (this.scoresScreen == null) {
			this.scoresScreen = new HighScoresScreen(this);
		}
		this.setScreen(this.scoresScreen);
	}

	/**
	 * Show the game over screen
	 * 
	 * @param time the number of seconds elapsed when the game finished
	 */
	public void showGameOver(float time) {
		if (this.gameOverScreen == null) {
			this.gameOverScreen = new GameOverScreen(this);
		}

		this.gameOverScreen.setTimerValue(time);
		this.setScreen(this.gameOverScreen);
	}
}
