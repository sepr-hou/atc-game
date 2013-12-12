package seprhou.gui;

public class GameScreen extends AbstractScreen
{
	public GameScreen(AtcGame game) {
		super(game);

		GameArea gameArea = new GameArea();
		gameArea.setBounds(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
		defaultStage.addActor(gameArea);
	}
}
