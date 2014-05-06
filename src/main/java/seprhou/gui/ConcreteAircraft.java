package seprhou.gui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import seprhou.logic.*;

import java.util.Collections;

/** The only type of aircraft (currently?) available */
public class ConcreteAircraft extends Aircraft
{
	/** The AirspaceObjectFactory which can create concrete aircraft */
	public static final AirspaceObjectFactory FACTORY = new FactoryClass();

	// Derived constants
	// The +1 and -1 here are leeway needed due to float rounding errors
	private static final float AIRCRAFT_MIN_SPEED = Collections.min(LogicConstants.INITIAL_SPEEDS) - 10;
	private static final float AIRCRAFT_MAX_SPEED = Collections.max(LogicConstants.INITIAL_SPEEDS) + 10;
	private static final float AIRCRAFT_MIN_ALTITUDE = 5000f;
	private static final float AIRCRAFT_MAX_ALTITUDE = Collections.max(LogicConstants.INITIAL_ALTITUDES);
	private static final float SHADOW_HEIGHT_MULTIPLIER = 0.001f;
	private static final float SHADOW_ANGLE = 10.5f;
	private static final Vector2D SHADOW_DIRECTION = Vector2D.fromPolar(SHADOW_HEIGHT_MULTIPLIER, SHADOW_ANGLE);

	/** Creates a new concrete aircraft */
	private ConcreteAircraft(String name, float weight, int crew, int colour, FlightPlan flightPlan, Airspace airspace)
	{
		super(name, weight, crew, colour, flightPlan, 1000, airspace);
	}

	/**
	 * Factory class which can create aircraft from an aisrpace an a flight plan
	 */
	private static class FactoryClass implements AirspaceObjectFactory
	{
		@Override
		public AirspaceObject makeObject(Airspace airspace, FlightPlan flightPlan)
		{
			// Random flight number between YO000 and YO999
			String flightNumber = String.format("YO%03d", Utils.getRandom().nextInt(1000));
			int colour = Utils.getRandom().nextInt(2);

			return makeObject(airspace, flightPlan, flightNumber, colour);
		}

		@Override
		public AirspaceObject makeObject(Airspace airspace, FlightPlan flightPlan, String flightNumber, int colour)
		{
			return new ConcreteAircraft(flightNumber, 100, 5, colour, flightPlan, airspace);
		}
	}

	@Override
	public void draw(Object state)
	{
		GameArea gameArea = (GameArea) state;
		Batch batch = gameArea.getBatch();

		// Add parent X and Y since Batch does not adjust coordinates for the Actor
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
			if (colour == 1)
			{
				aircraftTexture = Assets.AIRCRAFT_TEXTURE_RED;
			}
			else
			{
				aircraftTexture = Assets.AIRCRAFT_TEXTURE_BLUE;	
			}
		}

		// Draw aircraft + its shadow
		drawAircraft(batch, Assets.AIRCRAFT_SHADOW,
				xPos + SHADOW_DIRECTION.getX() * altitude,
				yPos + SHADOW_DIRECTION.getY() * altitude);

		drawAircraft(batch, aircraftTexture, xPos, yPos);

		// Draw altitude in top right
		String str = (int) this.getAltitude() + "ft";
		Assets.FONT.draw(batch, str, xPos + this.getSize() * 2,
				yPos + this.getSize() * 2);

		String str2 = Math.round(this.getVelocity().getLength() * 10) + "mph";
		Assets.FONT.draw(batch, str2, xPos + this.getSize() * 2, yPos + this.getSize() * 2 - 16);
	}

	private void drawAircraft(Batch batch, Texture texture, float xPos, float yPos)
	{
		float angleDegrees = getVelocity().getAngle() * (float) (180.0 / Math.PI);

		batch.draw(
				texture,                            // Aircraft texture
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
	}

	@Override public float getSize()             { return LogicConstants.AIRCRAFT_SIZE; }
	@Override public float getAscentRate()       { return LogicConstants.AIRCRAFT_ASCENT_RATE; }
	@Override public float getMaxTurnRate()      { return LogicConstants.AIRCRAFT_TURN_RATE; }

	@Override public float getMinSpeed()         { return AIRCRAFT_MIN_SPEED; }
	@Override public float getMaxSpeed()         { return AIRCRAFT_MAX_SPEED; }
	@Override public float getMinAltitude()      { return AIRCRAFT_MIN_ALTITUDE; }
	@Override public float getMaxAltitude()      { return AIRCRAFT_MAX_ALTITUDE; }

	// Change of speed now enabled!
	@Override public float getMaxAcceleration()  { return 10; }
}
