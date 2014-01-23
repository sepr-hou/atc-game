package seprhou.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The screen displaying the main menu
 */
public class MenuScreen extends AbstractScreen
{
	private static final float BUTTON_WIDTH = 300f;
	private static final float BUTTON_HEIGHT = 60f;
	private static final float BUTTON_SPACING = 10f;

	/**
	 * Initializes the menu screen
	 *
	 * @param game game which owns this screen
	 */
	public MenuScreen(AtcGame game)
	{
		super(game, false);
		Stage stage = getStage();

		// Put buttons in the middle of the screen in the x-axis
		final float buttonX = (SCREEN_WIDTH - BUTTON_WIDTH) / 2;

		// Start placing the label and buttons 100px above the centre of the screen in the y-axis
		float currentY = ((SCREEN_HEIGHT - BUTTON_HEIGHT)/2);

		// Clear the stage
		stage.clear();

		// Set background image
		Image backgroundImage = new Image(Assets.MENU_BACKGROUND_TEXTURE);
		stage.addActor(backgroundImage);

		// button "Start Game"setScreen(menuScreen);
		TextButton startGameButton = new TextButton("Start game", Assets.SKIN);
		startGameButton.setX(buttonX);
		startGameButton.setY(currentY);
		startGameButton.setWidth(BUTTON_WIDTH);
		startGameButton.setHeight(BUTTON_HEIGHT);
		startGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				getGame().showGame();
			}
		});
		stage.addActor(startGameButton);

		// button "Options"
		TextButton optionsButton = new TextButton("Options", Assets.SKIN);
		optionsButton.setX(buttonX);
		optionsButton.setY((currentY -= BUTTON_HEIGHT + BUTTON_SPACING));
		optionsButton.setWidth(BUTTON_WIDTH);
		optionsButton.setHeight(BUTTON_HEIGHT);
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				getGame().showOptions();
			}
		});
		stage.addActor(optionsButton);

		// button "High Scores"
		TextButton highScoresButton = new TextButton("High Scores", Assets.SKIN);
		highScoresButton.setX(buttonX);
		highScoresButton.setY((currentY -= BUTTON_HEIGHT + BUTTON_SPACING));
		highScoresButton.setWidth(BUTTON_WIDTH);
		highScoresButton.setHeight(BUTTON_HEIGHT);
		highScoresButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				getGame().showHighScores();
			}
		});
		stage.addActor(highScoresButton);

		// button "Exit"
		TextButton exitButton = new TextButton("Exit", Assets.SKIN);
		exitButton.setX(buttonX);
		exitButton.setY((currentY -= BUTTON_HEIGHT + BUTTON_SPACING));
		exitButton.setWidth(BUTTON_WIDTH);
		exitButton.setHeight(BUTTON_HEIGHT);
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				Gdx.app.exit();
			}
		});
		stage.addActor(exitButton);
	}
}