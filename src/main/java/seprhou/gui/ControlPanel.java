package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import seprhou.logic.*;

/**
 * The actor which displays the control panel on the right of the game screen
 */
public class ControlPanel extends Group
{
	private final GameScreen parent;
	private final Label valueFlightNo, valueAltitude, valueBearing, valueAirspeed,
			valueXPosition, valueYPosition, valueCrew, valueWeight, timerLabel;

	/**
	 * Creates a new ControlPanel
	 *
	 * @param parent parent GameScreen
	 */
	public ControlPanel(GameScreen parent)
	{
		this.parent = parent;
		
		float titleAlign = (float) 50.0;
		float valueAlign = (float) 80.0;
		float titleScale = (float) 1.3;
		
		Image gamelogo = new Image(Assets.GAME_TITLE);
		gamelogo.setX(titleAlign);
		gamelogo.setY((float) 850);
		this.addActor(gamelogo);

		Label nameFlightNo = new Label("Flight Number:", Assets.SKIN);
		nameFlightNo.setX(titleAlign);
		nameFlightNo.setY((float) 800.0);
		nameFlightNo.setFontScale(titleScale);
		this.addActor(nameFlightNo);
		valueFlightNo = new Label("", Assets.SKIN);
		valueFlightNo.setX(valueAlign);
		valueFlightNo.setY((float) 780.0);
		this.addActor(valueFlightNo);
		
		Label nameAltitude = new Label("Altitude:", Assets.SKIN);
		nameAltitude.setX(titleAlign);
		nameAltitude.setY((float) 720.0);
		nameAltitude.setFontScale(titleScale);
		this.addActor(nameAltitude);
		valueAltitude = new Label("", Assets.SKIN);
		valueAltitude.setX(valueAlign);
		valueAltitude.setY((float) 700.0);
		this.addActor(valueAltitude);
		
		Label nameBearing = new Label("Bearing:", Assets.SKIN);
		nameBearing.setX(titleAlign);
		nameBearing.setY((float) 640.0);
		nameBearing.setFontScale(titleScale);
		this.addActor(nameBearing);
		valueBearing = new Label("", Assets.SKIN);
		valueBearing.setX(valueAlign);
		valueBearing.setY((float) 620.0);
		this.addActor(valueBearing);
		
		Label nameAirspeed = new Label("Airspeed:", Assets.SKIN);
		nameAirspeed.setX(titleAlign);
		nameAirspeed.setY((float) 560.0);
		nameAirspeed.setFontScale(titleScale);
		this.addActor(nameAirspeed);
		valueAirspeed = new Label("", Assets.SKIN);
		valueAirspeed.setX(valueAlign);
		valueAirspeed.setY((float) 540.0);
		this.addActor(valueAirspeed);
		
		Label namePosition = new Label("Position:", Assets.SKIN);
		namePosition.setX(titleAlign);
		namePosition.setY((float) 480.0);
		namePosition.setFontScale(titleScale);
		this.addActor(namePosition);
		valueXPosition = new Label("", Assets.SKIN);
		valueXPosition.setX(valueAlign);
		valueXPosition.setY((float) 460.0);
		this.addActor(valueXPosition);
		valueYPosition = new Label("", Assets.SKIN);
		valueYPosition.setX(valueAlign);
		valueYPosition.setY((float) 440.0);
		this.addActor(valueYPosition);
		
		Label nameCrew = new Label("Crew:", Assets.SKIN);
		nameCrew.setX(titleAlign);
		nameCrew.setY((float) 380.0);
		nameCrew.setFontScale(titleScale);
		this.addActor(nameCrew);
		valueCrew = new Label("", Assets.SKIN);
		valueCrew.setX(valueAlign);
		valueCrew.setY((float) 360.0);
		this.addActor(valueCrew);
		
		Label nameWeight = new Label("Weight:", Assets.SKIN);
		nameWeight.setX(titleAlign);
		nameWeight.setY((float) 300.0);
		nameWeight.setFontScale(titleScale);
		this.addActor(nameWeight);
		valueWeight = new Label("", Assets.SKIN);
		valueWeight.setX(valueAlign);
		valueWeight.setY((float) 280.0);
		this.addActor(valueWeight);

		timerLabel = new Label("", Assets.SKIN);
		timerLabel.setX(valueAlign);
		timerLabel.setY((float) 160.0);
		timerLabel.setFontScale((float) 2.0);
		this.addActor(timerLabel);
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
		Aircraft selected = this.parent.getSelectedAircraft();
		if (selected != null){
			valueFlightNo.setText(selected.getName());
			valueAltitude.setText(Integer.toString(Math.round(selected.getAltitude())) + "ft");
			valueBearing.setText(Integer.toString(selected.getBearing()) + " degrees");
			valueAirspeed.setText(Integer.toString(Math.round(selected.getVelocity().getLength()))+ "mph");
			valueXPosition.setText("x = "+ Math.round(selected.getPosition().getX()));
			valueYPosition.setText("y = "+ Math.round(selected.getPosition().getY()));
			valueCrew.setText(Integer.toString(selected.getCrew()));
			valueWeight.setText(Float.toString(selected.getWeight()) + "kg");
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
	}
}
