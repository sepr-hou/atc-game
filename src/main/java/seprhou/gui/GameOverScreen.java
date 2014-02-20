package seprhou.gui;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import seprhou.logic.HighscoresDB;
import seprhou.logic.Utils;

/**
 * The screen displayed after the game is over
 */
public class GameOverScreen extends AbstractScreen {
	private final Label timerLabel;

	public GameOverScreen(AtcGame game) {
		super(game);
		Stage stage = this.getStage();

		// Background image
		Image gameOverImage = new Image(Assets.GAMEOVER_TEXTURE);
		stage.addActor(gameOverImage);

		// Final timer value
		this.timerLabel = new Label("", Assets.SKIN);
		this.timerLabel.setPosition(500, 300);
		stage.addActor(this.timerLabel);
	}

	/**
	 * Sets the number of seconds and score to show on the game screen as the
	 * final time 
	 * Also writes the current score to the high score database.
	 * @param value time to show in seconds
	 */
	public void setTimerValue(float time, int score) {
		// Update timer label
		this.timerLabel.setText("Your final time was: "
				+ Utils.formatTime(time) + "\n"
				+ "Your final score was: " + score + "\n"
				+ "Press escape to return to the main menu");

		try {
			PreparedStatement s = HighscoresDB.getConnection().prepareStatement("INSERT INTO highscores (score) VALUES (?)");
			s.setInt(1, score);
			s.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
