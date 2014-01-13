package seprhou.gui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import seprhou.logic.Aircraft;
import seprhou.logic.Waypoint;

import java.util.List;

/** The only type of aircraft (currently?) available */
public class ConcreteAircraft extends Aircraft
{
	public ConcreteAircraft(String name, float weight, int crew, List<Waypoint> flightPlan)
	{
		super(name, weight, crew, flightPlan);
	}

	@Override
	public void draw(Object state)
	{
		SpriteBatch batch = (SpriteBatch) state;
		float angleDegrees = getVelocity().getAngle() * (float) (180.0 / Math.PI);

		// TODO add GameArea.getX and GameArea.getY ?????
		//// Add parent X and Y since SpriteBatch does not adjust coordinates for the Actor
		//float xPos = GameArea.this.getX() + getPosition().getX() - getSize();
		//float yPos = GameArea.this.getY() + getPosition().getY() - getSize();

		float xPos = getPosition().getX() - getSize();
		float yPos = getPosition().getY() - getSize();

		batch.draw(
				Assets.AIRCRAFT_TEXTURE,            // Aircraft texture
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

	@Override public float getSize()                { return 64; }
	@Override protected float getAscentRate()       { return 10; }
	@Override protected float getMinSpeed()         { return 0; }
	@Override protected float getMaxSpeed()         { return 100; }
	@Override protected float getMaxAltitude()      { return 100; }
	@Override protected float getMaxAcceleration()  { return 10; }
	@Override protected float getMaxTurnRate()      { return 10; }
}
