package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import seprhou.logic.Aircraft;
import seprhou.logic.Utils;

/**
 * The actor which displays the control panel on the right of the game screen
 */
public class ControlPanel extends Group
{
	private final GameScreen parent;
	private final float titleAlign = 50.0f;
	private final float valueAlign = 80.0f;
	private final float titleScale = 1.3f;

	private final Label valueFlightNo, valueAltitude, valueBearing, valueAirspeed,
			valueXPosition, valueYPosition, valueCrew, valueWeight, timerLabel, valueScore;

	/**
	 * Creates a new ControlPanel
	 *
	 * @param parent parent GameScreen
	 */
	public ControlPanel(GameScreen parent)
	{
		this.parent = parent;

		Image gamelogo = new Image(Assets.GAME_TITLE);
		gamelogo.setX(this.titleAlign);
		gamelogo.setY(850.0f);
		this.addActor(gamelogo);

		Label nameFlightNo = new Label("Flight Number:", Assets.SKIN);
		nameFlightNo.setX(this.titleAlign);
		nameFlightNo.setY(800.0f);
		nameFlightNo.setFontScale(this.titleScale);
		this.addActor(nameFlightNo);
		valueFlightNo = new Label("", Assets.SKIN);
		valueFlightNo.setX(valueAlign);
		valueFlightNo.setY(780.0f);
		this.addActor(valueFlightNo);

		Label nameAltitude = new Label("Altitude:", Assets.SKIN);
		nameAltitude.setX(this.titleAlign);
		nameAltitude.setY(720.0f);
		nameAltitude.setFontScale(this.titleScale);
		this.addActor(nameAltitude);

		valueAltitude = new Label("", Assets.SKIN);
		valueAltitude.setX(valueAlign);
		valueAltitude.setY(700.0f);
		this.addActor(valueAltitude);

		Label nameBearing = new Label("Bearing:", Assets.SKIN);
		nameBearing.setX(this.titleAlign);
		nameBearing.setY(640.0f);
		nameBearing.setFontScale(this.titleScale);
		this.addActor(nameBearing);

		valueBearing = new Label("", Assets.SKIN);
		valueBearing.setX(valueAlign);
		valueBearing.setY(620.0f);
		this.addActor(valueBearing);

		Label nameAirspeed = new Label("Airspeed:", Assets.SKIN);
		nameAirspeed.setX(this.titleAlign);
		nameAirspeed.setY(560.0f);
		nameAirspeed.setFontScale(this.titleScale);
		this.addActor(nameAirspeed);

		valueAirspeed = new Label("", Assets.SKIN);
		valueAirspeed.setX(valueAlign);
		valueAirspeed.setY(540.0f);
		this.addActor(valueAirspeed);

		Label namePosition = new Label("Position:", Assets.SKIN);
		namePosition.setX(this.titleAlign);
		namePosition.setY(480.0f);
		namePosition.setFontScale(this.titleScale);
		this.addActor(namePosition);

		valueXPosition = new Label("", Assets.SKIN);
		valueXPosition.setX(valueAlign);
		valueXPosition.setY(460.0f);
		this.addActor(valueXPosition);
		valueYPosition = new Label("", Assets.SKIN);
		valueYPosition.setX(valueAlign);
		valueYPosition.setY(440.0f);
		this.addActor(valueYPosition);

		Label nameCrew = new Label("Crew:", Assets.SKIN);
		nameCrew.setX(this.titleAlign);
		nameCrew.setY(380.0f);
		nameCrew.setFontScale(this.titleScale);
		this.addActor(nameCrew);

		valueCrew = new Label("", Assets.SKIN);
		valueCrew.setX(valueAlign);
		valueCrew.setY(360.0f);
		this.addActor(valueCrew);

		Label nameWeight = new Label("Weight:", Assets.SKIN);
		nameWeight.setX(this.titleAlign);
		nameWeight.setY(300.0f);
		nameWeight.setFontScale(this.titleScale);
		this.addActor(nameWeight);

		valueWeight = new Label("", Assets.SKIN);
		valueWeight.setX(valueAlign);
		valueWeight.setY(280.0f);
		this.addActor(valueWeight);

		timerLabel = new Label("", Assets.SKIN);
		timerLabel.setX(valueAlign);
		timerLabel.setY(160.0f);
		timerLabel.setFontScale(2.0f);
		this.addActor(timerLabel);

		Label nameScore = new Label("Score:", Assets.SKIN);
		nameScore.setX(this.titleAlign);
		nameScore.setY(100.0f);
		nameScore.setFontScale(this.titleScale);
		this.addActor(nameScore);
		this.valueScore = new Label("", Assets.SKIN);
		this.valueScore.setX(this.valueAlign);
		this.valueScore.setY(70.0f);
		this.valueScore.setFontScale(2.0f);
		this.addActor(this.valueScore);
	}

	/**
	 * Returns the game screen used by the panel
	 */
	public GameScreen getGameScreen()
	{
		return parent;
	}

	@Override
	public void act(float delta)
	{
		Aircraft selected = parent.getSelectedAircraft();

		if (selected != null){
			valueFlightNo.setText(selected.getName());
			valueAltitude.setText(Integer.toString(Math.round(selected.getAltitude())) + " ft");
			valueBearing.setText(Integer.toString(Math.round(selected.getBearing())) + " degrees");
			valueAirspeed.setText(Integer.toString(Math.round(selected.getVelocity().getLength() * 10))+ " mph");
			valueXPosition.setText("x = "+ Math.round(selected.getPosition().getX()));
			valueYPosition.setText("y = "+ Math.round(selected.getPosition().getY()));
			valueCrew.setText(Integer.toString(selected.getCrew()));
			valueWeight.setText(Integer.toString(Math.round(selected.getWeight())) + " tonnes");
		} else {
			valueFlightNo.setText("");
			valueAltitude.setText("");
			valueBearing.setText("");
			valueAirspeed.setText("");
			valueXPosition.setText("");
			valueYPosition.setText("");
			valueCrew.setText("");
			valueWeight.setText("");
		}

		// Update timer
		timerLabel.setText(Utils.formatTime(parent.getSecondsSinceStart()));

		// Update score
		valueScore.setText(Integer.toString(parent.getScore()));
	}
}
