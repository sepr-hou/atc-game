package seprhou.gui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.GL20;

public class GameScreen extends AbstractScreen{

	
	public GameScreen(AtcGame game) {
		super(game);
	
		// TODO Auto-generated constructor stub
		defaultStage.addActor(new GameArea());
	}
	
	
	
}
