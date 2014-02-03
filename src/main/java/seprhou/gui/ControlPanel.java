package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import seprhou.logic.Aircraft;
import seprhou.logic.Utils;

/**
 * The actor which displays the control panel on the right of the game screen
 */
public class ControlPanel extends Group {
	private final GameScreen parent;
	private final float titleAlign = 50.0f, valueAlign = 80.0f, titleScale = 1.3f;;
	private final Label valueFlightNo, valueAltitude, valueBearing, valueAirspeed, valueXPosition, valueYPosition, valueCrew, valueWeight, timerLabel, valueScore;

	/**
	 * Creates a new ControlPanel
	 * 
	 * @param parent parent GameScreen
	 */
	public ControlPanel(GameScreen parent) {
		this.parent = parent;

		Image gamelogo = new Image(Assets.GAME_TITLE);
		gamelogo.setX(titleAlign);
		gamelogo.setY(850.0f);
		this.addActor(gamelogo);

		Label nameFlightNo = new Label("Flight Number:", Assets.SKIN);
		nameFlightNo.setX(titleAlign);
		nameFlightNo.setY(800.0f);
		nameFlightNo.setFontScale(titleScale);
		this.addActor(nameFlightNo);
		this.valueFlightNo = new Label("", Assets.SKIN);
		this.valueFlightNo.setX(valueAlign);
		this.valueFlightNo.setY(780.0f);
		this.addActor(this.valueFlightNo);

		Label nameAltitude = new Label("Altitude:", Assets.SKIN);
		nameAltitude.setX(titleAlign);
		nameAltitude.setY(720.0f);
		nameAltitude.setFontScale(titleScale);
		this.addActor(nameAltitude);
		this.valueAltitude = new Label("", Assets.SKIN);
		this.valueAltitude.setX(valueAlign);
		this.valueAltitude.setY(700.0f);
		this.addActor(this.valueAltitude);

		Label nameBearing = new Label("Bearing:", Assets.SKIN);
		nameBearing.setX(titleAlign);
		nameBearing.setY(640.0f);
		nameBearing.setFontScale(titleScale);
		this.addActor(nameBearing);
		this.valueBearing = new Label("", Assets.SKIN);
		this.valueBearing.setX(valueAlign);
		this.valueBearing.setY(620.0f);
		this.addActor(this.valueBearing);

		Label nameAirspeed = new Label("Airspeed:", Assets.SKIN);
		nameAirspeed.setX(titleAlign);
		nameAirspeed.setY(560.0f);
		nameAirspeed.setFontScale(titleScale);
		this.addActor(nameAirspeed);
		this.valueAirspeed = new Label("", Assets.SKIN);
		this.valueAirspeed.setX(valueAlign);
		this.valueAirspeed.setY(540.0f);
		this.addActor(this.valueAirspeed);

		Label namePosition = new Label("Position:", Assets.SKIN);
		namePosition.setX(titleAlign);
		namePosition.setY(480.0f);
		namePosition.setFontScale(titleScale);
		this.addActor(namePosition);
		this.valueXPosition = new Label("", Assets.SKIN);
		this.valueXPosition.setX(valueAlign);
		this.valueXPosition.setY(460.0f);
		this.addActor(this.valueXPosition);
		this.valueYPosition = new Label("", Assets.SKIN);
		this.valueYPosition.setX(valueAlign);
		this.valueYPosition.setY(440.0f);
		this.addActor(this.valueYPosition);

		Label nameCrew = new Label("Crew:", Assets.SKIN);
		nameCrew.setX(titleAlign);
		nameCrew.setY(380.0f);
		nameCrew.setFontScale(titleScale);
		this.addActor(nameCrew);
		this.valueCrew = new Label("", Assets.SKIN);
		this.valueCrew.setX(valueAlign);
		this.valueCrew.setY(360.0f);
		this.addActor(this.valueCrew);

		Label nameWeight = new Label("Weight:", Assets.SKIN);
		nameWeight.setX(titleAlign);
		nameWeight.setY(300.0f);
		nameWeight.setFontScale(titleScale);
		this.addActor(nameWeight);
		this.valueWeight = new Label("", Assets.SKIN);
		this.valueWeight.setX(valueAlign);
		this.valueWeight.setY(280.0f);
		this.addActor(this.valueWeight);

		this.timerLabel = new Label("", Assets.SKIN);
		this.timerLabel.setX(valueAlign);
		this.timerLabel.setY(160.0f);
		this.timerLabel.setFontScale(2.0f);
		this.addActor(this.timerLabel);
		
		Label nameScore = new Label("Score:", Assets.SKIN);
		nameScore.setX(titleAlign);
		nameScore.setY(100.0f);
		nameScore.setFontScale(titleScale);
		this.addActor(nameScore);
		this.valueScore = new Label("", Assets.SKIN);
		this.valueScore.setX(valueAlign);
		this.valueScore.setY(70.0f);
		this.valueScore.setFontScale(2.0f);
		this.addActor(this.valueScore);
	}

	/**
	 * Returns the game screen used by the panel
	 */
	public GameScreen getGameScreen() {
		return this.parent;
	}

	@Override
	public void act(float delta) {
		Aircraft selected = this.parent.getSelectedAircraft();
		if (selected != null) {
			this.valueFlightNo.setText(selected.getName());
			this.valueAltitude.setText(Integer.toString(Math.round(selected.getAltitude())) + " ft");
			this.valueBearing.setText(Integer.toString(Math.round(selected.getBearing())) + " degrees");
			this.valueAirspeed.setText(Integer.toString(Math.round(selected.getVelocity().getLength() * 10)) + " mph");
			this.valueXPosition.setText("x = " + Math.round(selected.getPosition().getX()));
			this.valueYPosition.setText("y = " + Math.round(selected.getPosition().getY()));
			this.valueCrew.setText(Integer.toString(selected.getCrew()));
			this.valueWeight.setText(Integer.toString(Math.round(selected.getWeight())) + " tonnes");

		} else {
			this.valueFlightNo.setText("");
			this.valueAltitude.setText("");
			this.valueBearing.setText("");
			this.valueAirspeed.setText("");
			this.valueXPosition.setText("");
			this.valueYPosition.setText("");
			this.valueCrew.setText("");
			this.valueWeight.setText("");
		}

		// Update timer
		this.timerLabel.setText(Utils.formatTime(this.parent.getSecondsSinceStart()));
		// Update score
		this.valueScore.setText(Integer.toString(this.parent.getScore()));
	}
}
