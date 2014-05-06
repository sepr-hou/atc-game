package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Class used by the main, connecting, highscores and options screens to help the layout of buttons
 */
public class ButtonLayoutHelper
{
	private static final float BUTTON_WIDTH = 300f;
	private static final float BUTTON_HEIGHT = 60f;

	// Put buttons in the middle of the screen in the x-axis
	private static final float BUTTON_XPOS = (AbstractScreen.SCREEN_WIDTH - BUTTON_WIDTH) / 2;

	// Start placing the label and buttons 100px above the centre of the screen in the y-axis
	private static final float BUTTON_INITIAL_YPOS = (AbstractScreen.SCREEN_HEIGHT - BUTTON_HEIGHT) / 2;

	private final Stage stage;
	private final float buttonSpacing;

	private float nextYPos = BUTTON_INITIAL_YPOS;

	/**
	 * Initializes a new ButtonLayoutHelper
	 * @param stage stage to add buttons to
	 */
	public ButtonLayoutHelper(Stage stage, float spacing)
	{
		this.stage = stage;
		this.buttonSpacing = spacing;
	}
	
	public ButtonLayoutHelper(Stage stage, float spacing, float ypos)
	{
		this.stage = stage;
		this.buttonSpacing = spacing;
		this.nextYPos = ypos;
	}

	/**
	 * Creates a new button on the menu
	 *
	 * @param name name of the button (text as it will appear)
	 * @param listener listener to handle click events
	 * @return the new button
	 */
	public TextButton createButton(String name, ClickListener listener)
	{
		return layoutActor(new TextButton(name, Assets.SKIN), listener);
	}

	/**
	 * Creates a text box on the menu
	 *
	 * @param text initial text in the text box
	 * @param listener listener to handle click events
	 * @return the new text box
	 */
	public TextField createField(String text, ClickListener listener)
	{
		return layoutActor(new TextField(text, Assets.SKIN), listener);
	}

	/**
	 * Adds a custom actor to column of buttons
	 *
	 * @param actor actor to add
	 * @param listener actor listener
	 * @param <T> type of actor to add
	 * @return the actor
	 */
	public <T extends Actor> T layoutActor(T actor, ClickListener listener)
	{
		actor.setBounds(BUTTON_XPOS, nextYPos, BUTTON_WIDTH, BUTTON_HEIGHT);
		nextYPos -= BUTTON_HEIGHT + buttonSpacing;

		if (listener != null)
			actor.addListener(listener);

		stage.addActor(actor);
		return actor;
	}
}
