package seprhou.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class MenuScreen extends AbstractScreen {

	public MenuScreen(AtcGame game) {
		super(game);
	}

	private static final float BUTTON_WIDTH = 300f;
	private static final float BUTTON_HEIGHT = 60f;
	private static final float BUTTON_SPACING = 10f;

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
		
		// Put buttons in the middle of the screen in the x-axis
		final float buttonX = (SCREEN_WIDTH - BUTTON_WIDTH) / 2;
		
		// Start placing the label and buttons 100px above the centre of the screen in the y-axis
		float currentY = ((SCREEN_HEIGHT - BUTTON_HEIGHT)/2);
		
		// Clear the stage
		defaultStage.clear();
		
		// Set background image
		Image backgroundImage = new Image(Assets.MENU_BACKGROUND_TEXTURE);
		defaultStage.addActor(backgroundImage);

		// button "Start Game"setScreen(menuScreen);
		TextButton startGameButton = new TextButton("Start game", Assets.SKIN);
		startGameButton.setX(buttonX);
		startGameButton.setY(currentY);
		startGameButton.setWidth(BUTTON_WIDTH);
		startGameButton.setHeight(BUTTON_HEIGHT);
		startGameButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new GameScreen(game));
			}
		});
		defaultStage.addActor(startGameButton);

		// button "Options"
		TextButton optionsButton = new TextButton("Options", Assets.SKIN);
		optionsButton.setX(buttonX);
		optionsButton.setY((currentY -= BUTTON_HEIGHT + BUTTON_SPACING));
		optionsButton.setWidth(BUTTON_WIDTH);
		optionsButton.setHeight(BUTTON_HEIGHT);
		optionsButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new OptionsScreen(game));
			}
		});
		defaultStage.addActor(optionsButton);

		// button "High Scores"
		TextButton highScoresButton = new TextButton("High Scores", Assets.SKIN);
		highScoresButton.setX(buttonX);
		highScoresButton.setY((currentY -= BUTTON_HEIGHT + BUTTON_SPACING));
		highScoresButton.setWidth(BUTTON_WIDTH);
		highScoresButton.setHeight(BUTTON_HEIGHT);
		highScoresButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				game.setScreen(new HighScoresScreen(game));
			}
		});
		defaultStage.addActor(highScoresButton);

		Gdx.input.setInputProcessor(defaultStage);
	}

}