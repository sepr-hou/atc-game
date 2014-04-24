package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class NetworkConfigScreen extends AbstractScreen{

	public NetworkConfigScreen(AtcGame game) {
		super(game);
				
		// Set background image
		getStage().addActor(new Image(Assets.MENU_BACKGROUND_TEXTURE));
		
		ButtonLayoutHelper layout = new ButtonLayoutHelper(getStage(), 10);
		
		layout.createButton("Connect", new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				// TODO
			}
		});
		
		layout.createButton("Host", new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				// TODO
			}
		});
	}
	
	
}
