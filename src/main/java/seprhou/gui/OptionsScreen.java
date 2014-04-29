package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Options screen
 */
public class OptionsScreen extends AbstractScreen
{
	/** Minimum lateral separation for collision warnings */
	private static float lateral = 200.0f;

	/** Minimum vertical separation for collision warnings */
	private static float vertical = 1000.0f;

	/**
	 * Initializes the options screen
	 *
	 * @param game game which owns this screen
	 */
	public OptionsScreen(AtcGame game)
	{
		super(game);

		// Set background image
		getStage().addActor(new Image(Assets.MENU_BACKGROUND_TEXTURE));

		// Add buttons
		ButtonLayoutHelper layout = new ButtonLayoutHelper(getStage(), 10);

		layout.createButton("Easy",   new DifficultyClickListener(100, 500));
		layout.createButton("Medium", new DifficultyClickListener(200, 1000));
		layout.createButton("Hard",   new DifficultyClickListener(300, 1500));
		layout.createButton("Back", new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				getGame().showMenu();
			}
		});
	}

	/** Returns the current lateral separation (based on difficulty) */
	public static float getLateral() { return lateral; }

	/** Returns the current vertical separation (based on difficulty) */
	public static float getVertical() { return vertical; }

	/**
	 * ClickListener which sets the difficulty + goes back to the main menu when clicked
	 */
	private class DifficultyClickListener extends ClickListener
	{
		private final float myLateral, myVertical;

		public DifficultyClickListener(float lateral, float vertical)
		{
			this.myLateral = lateral;
			this.myVertical = vertical;
		}

		@Override
		public void clicked(InputEvent event, float x, float y)
		{
			OptionsScreen.lateral = myLateral;
			OptionsScreen.vertical = myVertical;
			getGame().showMenu();
		}
	}
}
