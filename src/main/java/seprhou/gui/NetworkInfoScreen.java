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
 * Screen which handles the informational messages before a multiplayer game starts
 */
public class NetworkInfoScreen extends AbstractScreen
{
	public final Label text;

	public NetworkInfoScreen(AtcGame game, String textforlabel, final GameEndpoint endpoint)
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

		ButtonLayoutHelper layout = new ButtonLayoutHelper(getStage(), 10);

		// If the endpoint is not null, process the connection
		String cancelButtonText;
		if (endpoint != null)
		{
			// Add connection actor
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

			cancelButtonText = "Cancel";
		}
		else
		{
			cancelButtonText = "Back";
		}

		// Add cancel button
		layout.createButton(cancelButtonText, new ClickListener()
		{
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				if (endpoint != null)
					endpoint.close();

				getGame().showNetworkConfig();
			}
		});
	}
}
