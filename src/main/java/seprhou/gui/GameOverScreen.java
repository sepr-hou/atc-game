package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import seprhou.highscores.HighscoreEntry;
import seprhou.logic.Utils;

/**
 * The screen displayed after the game is over
 */
public class GameOverScreen extends AbstractScreen
{
	private final Label timerLabel;

	public GameOverScreen(AtcGame game)
	{
		super(game);
		Stage stage = getStage();

		// Background image
		Image gameOverImage = new Image(Assets.GAMEOVER_TEXTURE);
		stage.addActor(gameOverImage);

		// Final timer value
		timerLabel = new Label("", Assets.SKIN);
		timerLabel.setPosition(500, 300);
		stage.addActor(timerLabel);
	}

	/**
	 * Sets the number of seconds and score to show on the game screen as the
	 * final time
	 * Also writes the current score to the high score database.
	 *
	 * @param time the final time of the player (in seconds)
	 * @param score the final score of the player
	 */
	public void setTimerValue(float time, int score) {
		// Update timer label
		this.timerLabel.setText("Your final time was: "
				+ Utils.formatTime(time) + "\n"
				+ "Your final score was: " + score + "\n"
				+ "Press escape to return to the main menu");

		// Write to highscores
		getGame().getGlobalScores().addScore(new HighscoreEntry(score));
	}
}
