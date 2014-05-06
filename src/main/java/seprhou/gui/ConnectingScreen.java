package seprhou.gui;

import seprhou.network.GameEndpoint;
import seprhou.network.GameEndpointState;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

/**
 * Screen which handles the "connecting" message before a multiplayer game starts
 */
public class ConnectingScreen extends AbstractScreen
{
	public final Label text;

	public ConnectingScreen(AtcGame game, String textforlabel, final GameEndpoint endpoint)
	{
		super(game);
		Stage stage = getStage();

		// Set background image
		stage.addActor(new Image(Assets.MENU_BACKGROUND_TEXTURE));

		text = new Label("", Assets.SKIN);
		text.setText(textforlabel);
		text.setPosition(850, 600);
		text.setFontScale(2f);
		text.setAlignment(1);
		stage.addActor(text);
		stage.addActor(new Actor()
		{
			@Override
			public void act(float delta)
			{
				endpoint.actBegin();

				if (endpoint.getState() == GameEndpointState.CONNECTED)
				{
					getGame().showGame(endpoint);
				}
				else if (endpoint.getState() == GameEndpointState.CLOSED)
				{
					text.setText("Connection Error");
				}
			}
		});

		ButtonLayoutHelper layout = new ButtonLayoutHelper(getStage(), 10);

		layout.createButton("Cancel", new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				endpoint.close();
				getGame().showNetworkConfig();
			}
		});
	}
}
