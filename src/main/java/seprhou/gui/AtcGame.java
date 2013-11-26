package seprhou.gui;

import com.badlogic.gdx.Game;

public class AtcGame extends Game {
	
	public MenuScreen menuScreen;
	
	@Override
	public void create() {
		// TODO Auto-generated method stub
		menuScreen = new MenuScreen(this);
		setScreen(menuScreen);
	}

}
