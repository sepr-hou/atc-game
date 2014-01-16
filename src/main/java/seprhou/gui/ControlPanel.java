package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.Group;
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
		
		Label logo = new Label("Aircraft Frenzy 6", Assets.SKIN);
		logo.setX((float) 50.0);
		logo.setY((float) 940.0);
		logo.setFontScale((float) 1.8);
		this.addActor(logo);

		Label nameFlightNo = new Label("Flight Number:", Assets.SKIN);
		nameFlightNo.setX((float) 50.0);
		nameFlightNo.setY((float) 800.0);
		this.addActor(nameFlightNo);
		valueFlightNo = new Label("", Assets.SKIN);
		valueFlightNo.setX((float) 55.0);
		valueFlightNo.setY((float) 780.0);
		this.addActor(valueFlightNo);
		
		Label nameAltitude = new Label("Altitude:", Assets.SKIN);
		nameAltitude.setX((float) 50.0);
		nameAltitude.setY((float) 740.0);
		this.addActor(nameAltitude);
		valueAltitude = new Label("", Assets.SKIN);
		valueAltitude.setX((float) 55.0);
		valueAltitude.setY((float) 720.0);
		this.addActor(valueAltitude);
		
		Label nameBearing = new Label("Bearing:", Assets.SKIN);
		nameBearing.setX((float) 50.0);
		nameBearing.setY((float) 680.0);
		this.addActor(nameBearing);
		valueBearing = new Label("", Assets.SKIN);
		valueBearing.setX((float) 55.0);
		valueBearing.setY((float) 660.0);
		this.addActor(valueBearing);
		
		Label nameAirspeed = new Label("Airspeed:", Assets.SKIN);
		nameAirspeed.setX((float) 50.0);
		nameAirspeed.setY((float) 620.0);
		this.addActor(nameAirspeed);
		valueAirspeed = new Label("", Assets.SKIN);
		valueAirspeed.setX((float) 55.0);
		valueAirspeed.setY((float) 600.0);
		this.addActor(valueAirspeed);
		
		Label namePosition = new Label("Position:", Assets.SKIN);
		namePosition.setX((float) 50.0);
		namePosition.setY((float) 560.0);
		this.addActor(namePosition);
		valueXPosition = new Label("", Assets.SKIN);
		valueXPosition.setX((float) 55.0);
		valueXPosition.setY((float) 540.0);
		this.addActor(valueXPosition);
		valueYPosition = new Label("", Assets.SKIN);
		valueYPosition.setX((float) 55.0);
		valueYPosition.setY((float) 520.0);
		this.addActor(valueYPosition);
		
		Label nameCrew = new Label("Crew:", Assets.SKIN);
		nameCrew.setX((float) 50.0);
		nameCrew.setY((float) 480.0);
		this.addActor(nameCrew);
		valueCrew = new Label("", Assets.SKIN);
		valueCrew.setX((float) 55.0);
		valueCrew.setY((float) 460.0);
		this.addActor(valueCrew);
		
		Label nameWeight = new Label("Weight:", Assets.SKIN);
		nameWeight.setX((float) 50.0);
		nameWeight.setY((float) 420.0);
		this.addActor(nameWeight);
		valueWeight = new Label("", Assets.SKIN);
		valueWeight.setX((float) 55.0);
		valueWeight.setY((float) 400.0);
		this.addActor(valueWeight);

		timerLabel = new Label("", Assets.SKIN);
		timerLabel.setX((float) 55.0);
		timerLabel.setY((float) 350.0);
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
			valueAirspeed.setText(Integer.toString(Math.round(selected.getVelocity().getLength()))+ "Km/h");
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
		timerLabel.setText(formatTime(parent.getSecondsSinceStart()));
	}

	private String formatTime(float time)
	{
		// Separate into minutes, seconds and tenths
		int minutes = ((int) time) / 60;
		int seconds = ((int) time) % 60;
		int tenths =  ((int) (time * 10)) % 10;

		return String.format("%02d:%02d.%d", minutes, seconds, tenths);
	}
}
