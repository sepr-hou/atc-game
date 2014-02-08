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
public class MenuScreen extends AbstractScreen {
	private static final float BUTTON_WIDTH = 300f;
	private static final float BUTTON_HEIGHT = 60f;
	private static final float BUTTON_SPACING = 10f;

	/**
	 * Initializes the menu screen
	 * 
	 * @param game game which owns this screen
	 */
	public MenuScreen(AtcGame game) {
		super(game, false);
		Stage stage = this.getStage();

		// Put buttons in the middle of the screen in the x-axis
		final float buttonX = (AbstractScreen.SCREEN_WIDTH - MenuScreen.BUTTON_WIDTH) / 2;

		// Start placing the label and buttons 100px above the centre of the
		// screen in the y-axis
		float currentY = (AbstractScreen.SCREEN_HEIGHT - MenuScreen.BUTTON_HEIGHT) / 2;

		// Clear the stage
		stage.clear();

		// Set background image
		Image backgroundImage = new Image(Assets.MENU_BACKGROUND_TEXTURE);
		stage.addActor(backgroundImage);

		// button "Start Game"setScreen(menuScreen);
		TextButton startGameButton = new TextButton("Start game", Assets.SKIN);
		startGameButton.setX(buttonX);
		startGameButton.setY(currentY);
		startGameButton.setWidth(MenuScreen.BUTTON_WIDTH);
		startGameButton.setHeight(MenuScreen.BUTTON_HEIGHT);
		startGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MenuScreen.this.getGame().showGame();
			}
		});
		stage.addActor(startGameButton);

		// button "Options"
		TextButton optionsButton = new TextButton("Options", Assets.SKIN);
		optionsButton.setX(buttonX);
		optionsButton.setY(currentY -= MenuScreen.BUTTON_HEIGHT
				+ MenuScreen.BUTTON_SPACING);
		optionsButton.setWidth(MenuScreen.BUTTON_WIDTH);
		optionsButton.setHeight(MenuScreen.BUTTON_HEIGHT);
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MenuScreen.this.getGame().showOptions();
			}
		});
		stage.addActor(optionsButton);

		// button "High Scores"
		TextButton highScoresButton = new TextButton("High Scores", Assets.SKIN);
		highScoresButton.setX(buttonX);
		highScoresButton.setY(currentY -= MenuScreen.BUTTON_HEIGHT
				+ MenuScreen.BUTTON_SPACING);
		highScoresButton.setWidth(MenuScreen.BUTTON_WIDTH);
		highScoresButton.setHeight(MenuScreen.BUTTON_HEIGHT);
		highScoresButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				MenuScreen.this.getGame().showHighScores();
			}
		});
		stage.addActor(highScoresButton);

		// button "Exit"
		TextButton exitButton = new TextButton("Exit", Assets.SKIN);
		exitButton.setX(buttonX);
		exitButton.setY(currentY -= MenuScreen.BUTTON_HEIGHT
				+ MenuScreen.BUTTON_SPACING);
		exitButton.setWidth(MenuScreen.BUTTON_WIDTH);
		exitButton.setHeight(MenuScreen.BUTTON_HEIGHT);
		exitButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				Gdx.app.exit();
			}
		});
		stage.addActor(exitButton);
	}
}
