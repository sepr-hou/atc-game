package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Class used by the main and options menu to help the layout of buttons
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

	/**
	 * Creates a new button on the menu
	 *
	 * @param name name of the button (text as it will appear)
	 * @param listener listener to handle click events
	 * @return the new button
	 */
	public TextButton createButton(String name, ClickListener listener)
	{
		TextButton button = new TextButton(name, Assets.SKIN);

		button.setBounds(BUTTON_XPOS, nextYPos, BUTTON_WIDTH, BUTTON_HEIGHT);
		nextYPos -= BUTTON_HEIGHT + buttonSpacing;

		if (listener != null)
			button.addListener(listener);

		stage.addActor(button);
		return button;
	}

	public TextField createField(String name, ClickListener listener)
	{
		TextField field = new TextField(name, Assets.SKIN);

		field.setBounds(BUTTON_XPOS, nextYPos, BUTTON_WIDTH, BUTTON_HEIGHT);
		nextYPos -= BUTTON_HEIGHT + buttonSpacing;

		if (listener != null)
			field.addListener(listener);

		stage.addActor(field);
		return field;
	}
	
}
