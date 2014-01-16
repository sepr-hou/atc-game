package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

public class GameOverScreen extends AbstractScreen
{

	public GameOverScreen(AtcGame game) {
		super(game);
		// TODO Auto-generated constructor stub
		
		Image gameOverImage = new Image(Assets.GAMEOVER_TEXTURE);
		Stage stage = getStage();
		
		stage.clear();
		stage.addActor(gameOverImage);
	}

}
