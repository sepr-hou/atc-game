package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import seprhou.highscores.HighscoreEntry;

import java.text.SimpleDateFormat;

/**
 * Class which shows the highscores
 */
public class HighScoresScreen extends AbstractScreen {

	private final static float SCORE_SCALE = 1.3f;

	private final static float TABLE_WIDTH = 600;
	private final static float TABLE_PADDING = 10;

	private final static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	private final Table scoresTable;

	public HighScoresScreen(AtcGame game)
	{
		super(game);

		Stage stage = getStage();

		// Set background image
		stage.addActor(new Image(Assets.MENU_BACKGROUND_TEXTURE));

		// Create table to store labels into
		scoresTable = new Table(Assets.SKIN);
		scoresTable.setBounds((SCREEN_WIDTH - TABLE_WIDTH) / 2, SCREEN_HEIGHT/5, TABLE_WIDTH, 500);
		stage.addActor(scoresTable);
		
		ButtonLayoutHelper layout = new ButtonLayoutHelper(getStage(), 10, 100);

		layout.createButton("Back", new ClickListener() {
			@Override
			public void clicked(InputEvent event, float x, float y)
			{
				getGame().showMenu();
			}
		});
	}

	@Override
	public void show()
	{
		super.show();
		scoresTable.clear();

		// Add headings
		scoresTable.add("Score");
		scoresTable.add("Date");
		scoresTable.row();

		// Add scores to the table
		for (HighscoreEntry entry : getGame().getGlobalScores().getScores())
		{
			addTableLabel(entry.getScore() + "");
			addTableLabel(DATE_FORMAT.format(entry.getDate()));
			scoresTable.row();
		}
	}
	
	private void addTableLabel(String str)
	{
		Label label = new Label(str, Assets.SKIN);
		label.setFontScale(SCORE_SCALE);
		scoresTable.add(label).pad(TABLE_PADDING);
	}
	
}
