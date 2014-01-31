package seprhou.gui;

import java.util.Collections;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import seprhou.logic.Aircraft;
import seprhou.logic.FlightPlan;

/** The only type of aircraft (currently?) available */
public class ConcreteAircraft extends Aircraft {
	// Derived constants
	// The +1 and -1 here are leeway needed due to float rounding errors
	private static final float AIRCRAFT_MIN_SPEED = Collections.min(Constants.INITIAL_SPEEDS) - 10;
	private static final float AIRCRAFT_MAX_SPEED = Collections.max(Constants.INITIAL_SPEEDS) + 10;
	private static final float AIRCRAFT_MIN_ALTITUDE = Collections.min(Constants.INITIAL_ALTITUDES);
	private static final float AIRCRAFT_MAX_ALTITUDE = Collections.max(Constants.INITIAL_ALTITUDES);

	public ConcreteAircraft(String name, float weight, int crew, FlightPlan flightPlan) {
		super(name, weight, crew, flightPlan, 1000);
	}

	@Override
	public void draw(Object state) {
		GameArea gameArea = (GameArea) state;
		SpriteBatch batch = gameArea.getBatch();

		float angleDegrees = this.getVelocity().getAngle() * (float) (180.0 / Math.PI);

		// Add parent X and Y since SpriteBatch does not adjust coordinates for
		// the Actor
		float xPos = gameArea.getX() + this.getPosition().getX() - this.getSize();
		float yPos = gameArea.getY() + this.getPosition().getY() - this.getSize();

		// Draw the aircraft
		Texture aircraftTexture;

		// If selected, use different colour plane
		if (this == gameArea.getGameScreen().getSelectedAircraft()) {
			aircraftTexture = Assets.AIRCRAFT_SELECTED;

			if (!this.getViolated()) {
				float circleRadius = Assets.CIRCLE_TEXTURE.getWidth() / 2;
				batch.draw(Assets.CIRCLE_TEXTURE, gameArea.getX() + this.getPosition().getX() - circleRadius, gameArea.getY() + this.getPosition().getY() - circleRadius);
			}
		} else {
			aircraftTexture = Assets.AIRCRAFT_TEXTURE;
		}

		batch.draw(aircraftTexture, // Aircraft texture
				xPos, // X position (bottom left)
				yPos, // Y position (bottom right)
				this.getSize(), // X rotation origin
				this.getSize(), // Y rotation origin
				this.getSize() * 2, // Width
				this.getSize() * 2, // Height
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

		// Draw altitude in top right
		String str = (int) this.getAltitude() + "ft";
		Assets.FONT.draw(batch, str, xPos + this.getSize() * 2, yPos + this.getSize() * 2);
	}

	@Override
	public float getSize() {
		return Constants.AIRCRAFT_SIZE;
	}

	@Override
	public float getAscentRate() {
		return Constants.AIRCRAFT_ASCENT_RATE;
	}

	@Override
	public float getMaxTurnRate() {
		return Constants.AIRCRAFT_TURN_RATE;
	}

	@Override
	public float getMinSpeed() {
		return ConcreteAircraft.AIRCRAFT_MIN_SPEED;
	}

	@Override
	public float getMaxSpeed() {
		return ConcreteAircraft.AIRCRAFT_MAX_SPEED;
	}

	@Override
	public float getMinAltitude() {
		return ConcreteAircraft.AIRCRAFT_MIN_ALTITUDE;
	}

	@Override
	public float getMaxAltitude() {
		return ConcreteAircraft.AIRCRAFT_MAX_ALTITUDE;
	}

	// Change of speed now enabled!
	@Override
	public float getMaxAcceleration() {
		return 10;
	}
}
