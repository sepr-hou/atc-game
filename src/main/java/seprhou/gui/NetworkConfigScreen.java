package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import seprhou.network.*;

/**
 * The network setup screen
 */
public class NetworkConfigScreen extends AbstractScreen
{
	private final TextField hostnameField;

	public NetworkConfigScreen(AtcGame game)
	{
		super(game);

		// Set background image
		getStage().addActor(new Image(Assets.MENU_BACKGROUND_TEXTURE));
		
		ButtonLayoutHelper layout = new ButtonLayoutHelper(getStage(), 10);
		
		layout.createButton("Connect", new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				GameEndpoint endpoint = new MultiClient(hostnameField.getText(),
						GameScreen.GAME_DIMENSIONS, ConcreteAircraft.FACTORY);

				getGame().showConnectingScreen("Connecting...", endpoint);
			}
		});
		
		hostnameField = layout.createField("Enter Host Name", null);
		
		layout.createButton("Host", new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				GameEndpoint endpoint = new MultiServer(
						GameScreen.GAME_DIMENSIONS,
						ConcreteAircraft.FACTORY,
						OptionsScreen.getLateral(),
						OptionsScreen.getVertical());

				getGame().showConnectingScreen("Waiting for someone to connect...", endpoint);
			}
		});
		
		layout.createButton("Back", new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				getGame().showMenu();
			}
		});
	}
}
