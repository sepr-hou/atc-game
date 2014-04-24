package seprhou.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * The screen displaying the main menu
 */
public class MenuScreen extends AbstractScreen
{
	/**
	 * Initializes the menu screen
	 *
	 * @param game game which owns this screen
	 */
	public MenuScreen(AtcGame game)
	{
		super(game, false);

		// Set background image
		getStage().addActor(new Image(Assets.MENU_BACKGROUND_TEXTURE));

		// Add buttons
		ButtonLayoutHelper layout = new ButtonLayoutHelper(getStage(), 10);

		layout.createButton("Start single player game", new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				getGame().showGame();
			}
		});
		
		layout.createButton("Start multiplayer game", new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				getGame().showNetworkConfig();
			}
		});

		layout.createButton("Options", new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				getGame().showOptions();
			}
		});

		layout.createButton("High Scores", new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				getGame().showHighScores();
			}
		});

		layout.createButton("Exit", new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				Gdx.app.exit();
			}
		});
	}
}
