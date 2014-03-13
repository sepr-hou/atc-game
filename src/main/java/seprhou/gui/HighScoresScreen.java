package seprhou.gui;

import java.util.List;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import seprhou.highscores.HighscoreEntry;

/**
 * Class which shows the highscores
 */
public class HighScoresScreen extends AbstractScreen {

	private final static float SCORE_SCALE = 1.3f;

	private final com.badlogic.gdx.scenes.scene2d.ui.List onScreenList;

	public HighScoresScreen(AtcGame game)
	{
		super(game);

		Stage stage = getStage();

		// Set background image
		stage.addActor(new Image(Assets.MENU_BACKGROUND_TEXTURE));

		// Create on screen list
		onScreenList = new com.badlogic.gdx.scenes.scene2d.ui.List(new Object[0], Assets.SKIN);
		stage.addActor(onScreenList);
	}

	@Override
	public void show()
	{
		super.show();

		// Generate labels for all the scores
		List<HighscoreEntry> scores = getGame().getGlobalScores().getScores();
		Label[] labels = new Label[scores.size()];

		for (int i = 0; i < labels.length; i++)
		{
			HighscoreEntry entry = scores.get(i);

			labels[i] = new Label(entry.getScore() + ", " + entry.getDate(), Assets.SKIN);
			labels[i].setFontScale(SCORE_SCALE);
			labels[i].setAlignment(Align.center);
		}

		// Write list to stage
		onScreenList.setItems(labels);
	}
}
