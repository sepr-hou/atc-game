package seprhou.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import seprhou.logic.Aircraft;
import seprhou.logic.Airspace;
import seprhou.logic.FlightPlan;
import seprhou.logic.Vector2D;

import java.util.Collections;

/** The only type of aircraft (currently?) available */
public class ConcreteAircraft extends Aircraft
{
	// Derived constants
	// The +1 and -1 here are leeway needed due to float rounding errors
	private static final float AIRCRAFT_MIN_SPEED = Collections.min(Constants.INITIAL_SPEEDS) - 10;
	private static final float AIRCRAFT_MAX_SPEED = Collections.max(Constants.INITIAL_SPEEDS) + 10;
	private static final float AIRCRAFT_MIN_ALTITUDE = 5000f;
	private static final float AIRCRAFT_MAX_ALTITUDE = Collections.max(Constants.INITIAL_ALTITUDES);
	private static final float SHADOW_HEIGHT_MULTIPLIER = 0.003f;
	private static final float SHADOW_ANGLE = 0.4f;
	private static final Vector2D SHADOW_DIRECTION = new Vector2D(
			(float) Math.sin(ConcreteAircraft.SHADOW_ANGLE),
			(float) Math.cos(ConcreteAircraft.SHADOW_ANGLE));

	public ConcreteAircraft(String name, float weight, int crew, FlightPlan flightPlan, Airspace airspace)
	{
		super(name, weight, crew, flightPlan, 1000, airspace);
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
		{
			aircraftTexture = Assets.AIRCRAFT_SELECTED;

			if (!this.isViolated())
			{
				float circleRadius = Assets.CIRCLE_TEXTURE.getWidth() / 2;
				batch.draw(Assets.CIRCLE_TEXTURE, gameArea.getX()
						+ this.getPosition().getX() - circleRadius,
						gameArea.getY() + this.getPosition().getY()
								- circleRadius);
			}
		}
		else
		{
			aircraftTexture = Assets.AIRCRAFT_TEXTURE;
		}

		batch.draw(Assets.AIRCRAFT_SHADOW, // Aircraft shadow texture
				xPos + SHADOW_DIRECTION.getX()
						* (SHADOW_HEIGHT_MULTIPLIER * altitude), // X
																	// position
																	// (bottom
																	// left)
				yPos + SHADOW_DIRECTION.getY()
						* (SHADOW_HEIGHT_MULTIPLIER * altitude), // Y
																	// position
																	// (bottom
																	// right)
				getSize(), // X rotation origin
				getSize(), // Y rotation origin
				getSize() * 2, // Width
				getSize() * 2, // Height
				1.0f, // X scaling
				1.0f, // Y scaling
				angleDegrees, // Rotation
				0, // X position in texture
				0, // Y position in texture
				Assets.AIRCRAFT_TEXTURE.getWidth(), // Width of source texture
				Assets.AIRCRAFT_TEXTURE.getHeight(),// Height of source texture
				false, // Flip in X axis
				false // Flip in Y axis
		);

		batch.draw(
				aircraftTexture,                    // Aircraft texture
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
		String str = (int) this.getAltitude() + "ft";
		Assets.FONT.draw(batch, str, xPos + this.getSize() * 2,
				yPos + this.getSize() * 2);

		String str2 = Math.round(this.getVelocity().getLength() * 10) + "mph";
		Assets.FONT.draw(batch, str2, xPos + this.getSize() * 2, yPos + this.getSize() * 2 - 16);
	}

	@Override public float getSize()             { return Constants.AIRCRAFT_SIZE; }
	@Override public float getAscentRate()       { return Constants.AIRCRAFT_ASCENT_RATE; }
	@Override public float getMaxTurnRate()      { return Constants.AIRCRAFT_TURN_RATE; }

	@Override public float getMinSpeed()         { return AIRCRAFT_MIN_SPEED; }
	@Override public float getMaxSpeed()         { return AIRCRAFT_MAX_SPEED; }
	@Override public float getMinAltitude()      { return AIRCRAFT_MIN_ALTITUDE; }
	@Override public float getMaxAltitude()      { return AIRCRAFT_MAX_ALTITUDE; }

	// Change of speed now enabled!
	@Override public float getMaxAcceleration()  { return 10; }
}
