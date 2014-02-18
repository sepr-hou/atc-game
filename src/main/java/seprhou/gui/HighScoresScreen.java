package seprhou.gui;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.Align;

import seprhou.logic.HighscoresDB;

/**
 * Empty class which could be used to show the high scores
 */
public class HighScoresScreen extends AbstractScreen {

	private final float titleScale = 1.3f;
	private Stage stage;

	public HighScoresScreen(AtcGame game) {
		super(game);

		this.stage = this.getStage();
	}

	@Override
	public void show() {
		super.show();

		this.stage.clear();
		// Set background image
		Image backgroundImage = new Image(Assets.MENU_BACKGROUND_TEXTURE);
		this.stage.addActor(backgroundImage);

		try {
			PreparedStatement p = HighscoresDB.getConnection().prepareStatement("SELECT * FROM highscores ORDER BY score DESC, t ASC LIMIT 10");
			ResultSet scores = p.executeQuery();

			Label scoreLbl;
			float y = 600.0f;
			while (scores.next()) {
				scoreLbl = new Label(scores.getInt("score") + ", " + scores.getString("t"), Assets.SKIN);
				scoreLbl.setFontScale(this.titleScale);
				scoreLbl.setAlignment(Align.center);
				scoreLbl.setX((AbstractScreen.SCREEN_WIDTH - scoreLbl.getWidth()) / 2);
				scoreLbl.setY(y);
				this.stage.addActor(scoreLbl);
				y -= 40.0f;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
