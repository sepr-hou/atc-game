package seprhou.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import seprhou.logic.LogicConstants;

/**
 * Implementation of the Screen interface which sets up classes common to all screens
 */
public abstract class AbstractScreen implements Screen
{
	public static final int SCREEN_WIDTH = 1680;
	public static final int SCREEN_HEIGHT = 1050;

	private final AtcGame game;
	private final Stage stage;
	private final boolean enableEscape;

	private final FPSLogger logger = new FPSLogger();

	/**
	 * Creates a new AbstractScreen owned by a game
	 *
	 * @param game game which owns this screen
	 */
	public AbstractScreen(AtcGame game)
	{
		this(game, true);
	}

	/**
	 * Creates a new screen which is owned by the game
	 *
	 * @param game game which owns this screen
	 * @param enableEscape true to enable the escape key (go back to main menu)
	 */
	public AbstractScreen(AtcGame game, boolean enableEscape)
	{
		this.game = game;
		this.enableEscape = enableEscape;

		// Create stage to draw everything with
		this.stage = new Stage(new StretchViewport(SCREEN_WIDTH, SCREEN_HEIGHT));
	}

	/** Returns the game which owns this screen */
	public AtcGame getGame()
	{
		return game;
	}

	/** Returns the stage where everything is drawn from */
	public Stage getStage()
	{
		return stage;
	}

	@Override
	public void show()
	{
		// Called when the screen is re-shown after being hidden

		// Setup cursor
		int xOffset = Assets.CURSOR_IMAGE.getWidth() / 2;
		int yOffset = Assets.CURSOR_IMAGE.getHeight() / 2;
		Gdx.input.setCursorImage(Assets.CURSOR_IMAGE, xOffset, yOffset);

		// Setup input processor (which is probably owned by another screen currently)
		Gdx.input.setInputProcessor(this.stage);
	}

	@Override
	public void render(float delta)
	{
		// If escape is pressed, go back to the menu
		if (enableEscape && Gdx.input.isKeyPressed(Keys.ESCAPE))
		{
			game.showMenu();
			return;
		}

		// Clears the screen with the given RGB colour (black)
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		// Update game state
		stage.act(delta);

		// Render game
		stage.draw();
		
		// Record frames per second and output to console
		if(LogicConstants.DEBUG)
			logger.log();
	}

	@Override
	public void resize(int width, int height)
	{
		// Tell the stage viewport about the change in screen size so it can update it's projections
		stage.getViewport().update(width, height);
	}

	// Empty methods
	@Override public void dispose() { }
	@Override public void hide() { }
	@Override public void pause() { }
	@Override public void resume() { }
}
