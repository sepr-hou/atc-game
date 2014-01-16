package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

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
	 * Sets the number of seconds to show on the game screen as the final time
	 *
	 * @param value time to show in seconds
	 */
	public void setTimerValue(float value)
	{
		// Update timer label
		timerLabel.setText("Your final time was: " + Utils.formatTime(value) + "\n" +
				"Press escape to return to the main menu");
	}
}
