package seprhou.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import seprhou.logic.Aircraft;
import seprhou.logic.FlightPlan;
import seprhou.logic.Utils;

/** The only type of aircraft (currently?) available */
public class ConcreteAircraft extends Aircraft
{
	// Derived constants
	private static final int AIRCRAFT_MIN_SPEED = Utils.min(Constants.INITIAL_SPEEDS);
	private static final int AIRCRAFT_MAX_SPEED = Utils.max(Constants.INITIAL_SPEEDS);
	private static final int AIRCRAFT_MIN_ATITUDE = Utils.min(Constants.INITIAL_ALTITUDES);
	private static final int AIRCRAFT_MAX_ALTITUDE = Utils.max(Constants.INITIAL_ALTITUDES);

	public ConcreteAircraft(String name, float weight, int crew, FlightPlan flightPlan)
	{
		super(name, weight, crew, flightPlan);
	}

	@Override
	public void draw(Object state)
	{
		GameArea gameArea = (GameArea) state;
		SpriteBatch batch = gameArea.getBatch();

		float angleDegrees = getVelocity().getAngle() * (float) (180.0 / Math.PI);

		// Add parent X and Y since SpriteBatch does not adjust coordinates for the Actor
		float xPos = gameArea.getX() + getPosition().getX() - getSize();
		float yPos = gameArea.getY() + getPosition().getY() - getSize();

		// Draw the aircraft
		Texture aircraftTexture;
		
		// If selected, use different colour plane
		if (this == gameArea.getGameScreen().getSelectedAircraft())
				aircraftTexture = Assets.AIRCRAFT_SELECTED;
		else
			aircraftTexture = Assets.AIRCRAFT_TEXTURE;
				
		
		batch.draw(
				
				aircraftTexture,            // Aircraft texture
				xPos,                               // X position (bottom left)
				yPos,                               // Y position (bottom right)
				getSize(),                          // X rotation origin
				getSize(),                          // Y rotation origin
				getSize() * 2,                      // Width
				getSize() * 2,                      // Height
				1.0f,                               // X scaling
				1.0f,                               // Y scaling
				angleDegrees,                       // Rotation
				0,                                  // X position in texture
				0,                                  // Y position in texture
				Assets.AIRCRAFT_TEXTURE.getWidth(), // Width of source texture
				Assets.AIRCRAFT_TEXTURE.getHeight(),// Height of source texture
				false,                              // Flip in X axis
				false                               // Flip in Y axis
		);

		// Draw altitude in top right
		String str = ((int) getAltitude()) + "ft";
		Assets.FONT.draw(batch, str, xPos + getSize() * 2, yPos + getSize() * 2);
	}

	@Override public float getSize()             { return Constants.AIRCRAFT_SIZE; }
	@Override public float getAscentRate()       { return Constants.AIRCRAFT_ASCENT_RATE; }
	@Override public float getMaxTurnRate()      { return Constants.AIRCRAFT_TURN_RATE; }

	@Override public float getMinSpeed()         { return AIRCRAFT_MIN_SPEED; }
	@Override public float getMaxSpeed()         { return AIRCRAFT_MAX_SPEED; }
	@Override public float getMinAltitude()      { return AIRCRAFT_MIN_ATITUDE; }
	@Override public float getMaxAltitude()      { return AIRCRAFT_MAX_ALTITUDE; }

	// Note currently changing speed of active aircraft is disabled
	@Override public float getMaxAcceleration()  { return 0; }
}
