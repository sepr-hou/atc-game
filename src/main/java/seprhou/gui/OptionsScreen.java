package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * options screen
 */
public class OptionsScreen extends AbstractScreen {

	private static final float BUTTON_WIDTH = 300f;
	private static final float BUTTON_HEIGHT = 60f;

	/** Minimum lateral separation for collision warnings */
	private static float LATERAL_SEPARATION = 200.0f;
	/** Minimum vertical separation for collision warnings */
	private static float VERTICAL_SEPARATION = 1000.0f;

	public static float getLateral() {
		return LATERAL_SEPARATION;
	}

	public static float getVertical() {
		return VERTICAL_SEPARATION;
	}

	public OptionsScreen(AtcGame game) {
		super(game);

		Stage stage = this.getStage();

		// Put buttons in the middle of the screen in the x-axis
		final float buttonX = (AbstractScreen.SCREEN_WIDTH - OptionsScreen.BUTTON_WIDTH) / 2;

		// Start placing the label and buttons 100px above the centre of the
		// screen in the y-axis
		float currentY = (AbstractScreen.SCREEN_HEIGHT - OptionsScreen.BUTTON_HEIGHT) / 2;

		// Set background image
		Image backgroundImage = new Image(Assets.MENU_BACKGROUND_TEXTURE);
		stage.addActor(backgroundImage);

		// button for smallest exclusion zone
		TextButton easyButton = new TextButton("Easy", Assets.SKIN);
		easyButton.setX(buttonX);
		easyButton.setY(currentY);
		easyButton.setWidth(OptionsScreen.BUTTON_WIDTH);
		easyButton.setHeight(OptionsScreen.BUTTON_HEIGHT);
		easyButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				LATERAL_SEPARATION = 100.0f;
				VERTICAL_SEPARATION = 500.0f;
			}
		});

		// button for medium exclusion zone
		TextButton mediumButton = new TextButton("Medium", Assets.SKIN);
		mediumButton.setX(buttonX);
		mediumButton.setY(currentY - BUTTON_HEIGHT * 2);
		mediumButton.setWidth(OptionsScreen.BUTTON_WIDTH);
		mediumButton.setHeight(OptionsScreen.BUTTON_HEIGHT);
		mediumButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				LATERAL_SEPARATION = 200.0f;
				VERTICAL_SEPARATION = 1000.0f;
			}
		});

		// button for Largest exclusion zone
		TextButton hardButton = new TextButton("Hard", Assets.SKIN);
		hardButton.setX(buttonX);
		hardButton.setY(currentY - BUTTON_HEIGHT * 4);
		hardButton.setWidth(OptionsScreen.BUTTON_WIDTH);
		hardButton.setHeight(OptionsScreen.BUTTON_HEIGHT);
		hardButton.addListener(new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y) {
				LATERAL_SEPARATION = 300.0f;
				VERTICAL_SEPARATION = 1500.0f;
			}
		});
		stage.addActor(easyButton);
		stage.addActor(mediumButton);
		stage.addActor(hardButton);

	}
}
