package seprhou.gui;

import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import seprhou.logic.Aircraft;
import seprhou.logic.AircraftColour;
import seprhou.logic.Utils;

/**
 * The actor which displays the control panel on the right of the game screen
 */
public class ControlPanel extends Group
{
	private static final float TITLE_ALIGN = 50.0f;
	private static final float VALUE_ALIGN = 80.0f;
	private static final float TITLE_SCALE = 1.3f;

	private final GameScreen parent;

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
		gamelogo.setPosition(TITLE_ALIGN, 840);
		addActor(gamelogo);

		valueFlightNo = createTitleAndValue(800, "Flight Number");
		valueAltitude = createTitleAndValue(720, "Altitude");
		valueBearing  = createTitleAndValue(640, "Bearing");
		valueAirspeed = createTitleAndValue(560, "Airspeed");

		createTitleLabel(480, "Position");
		valueXPosition = createValueLabel(460);
		valueYPosition = createValueLabel(440);

		valueCrew = createTitleAndValue(380, "Crew");
		valueWeight = createTitleAndValue(300, "Weight");

		timerLabel = createValueLabel(200);
		timerLabel.setFontScale(2);

		valueScore = createTitleAndValue(140, "Score");
		valueScore.setFontScale(2);

		AircraftColour colour = parent.getEndpoint().getMyColour();

		if (colour == AircraftColour.WHITE){
			Image plane = new Image(Assets.AIRCRAFT_TEXTURE);
			plane.setPosition(VALUE_ALIGN, 50);
			addActor(plane);}
		else if (colour == AircraftColour.BLUE){
			Image plane = new Image(Assets.AIRCRAFT_TEXTURE_BLUE);
			plane.setPosition(VALUE_ALIGN, 50);
			addActor(plane);}	

		else if (colour == AircraftColour.RED){
			Image plane = new Image(Assets.AIRCRAFT_TEXTURE_RED);
			plane.setPosition(VALUE_ALIGN, 50);
			addActor(plane);}	
	}

	/** Creates a new label for a value */
	private Label createValueLabel(float y)
	{
		Label label = new Label("", Assets.SKIN);
		label.setPosition(VALUE_ALIGN, y);
		addActor(label);
		return label;
	}

	/** Creates a new label for a title (to show what a value means) */
	private Label createTitleLabel(float y, String name)
	{
		Label label = new Label(name + ":", Assets.SKIN);
		label.setPosition(TITLE_ALIGN, y);
		label.setFontScale(TITLE_SCALE);
		addActor(label);
		return label;
	}

	/** Combined function to create both a title and value label. */
	private Label createTitleAndValue(float titleY, String name)
	{
		createTitleLabel(titleY, name);
		return createValueLabel(titleY - 20);
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
