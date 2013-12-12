package seprhou.logic;

/**
 * An object residing in the airspace (which can move and collide with things)
 *
 * <p>
 * The current state of an object is contained within its 2d position, 2d velocity and altitude.
 * Each refresh, the object's position is updated according to its velocity.
 * In addition to this, object's also have a target velocity and altitude.
 * Each refresh, the object's velocity and altitude are updated to get closer to the targets at a
 * rate specified by some of the abstract getters.
 *
 * <p>
 * This class contains lots of abstract methods. These all specify various physics properties
 * about the object, and are used in the refresh method to control how the position and velocity
 * are updated.
 */
public abstract class AirspaceObject
{
	// Current state of the motion of the aircraft (position + 2d velocity)
	protected Vector2D position = Vector2D.ZERO;
	protected Vector2D velocity = Vector2D.ZERO;
	protected float altitude;

	// Targets
	protected Vector2D targetVelocity;
	protected float targetAltitude;

	/**
	 * Constructs a new AirspaceObject with 0 initial values
	 */
	protected AirspaceObject()
	{
	}

	/** Returns this aircraft's current position */
	public Vector2D getPosition()
	{
		return position;
	}

	/** Returns this aircraft's current velocity */
	public Vector2D getVelocity()
	{
		return velocity;
	}

	/** Returns this aircraft's current altitude */
	public float getAltitude()
	{
		return altitude;
	}

	/** Returns this aircraft's target velocity */
	public Vector2D getTargetVelocity()
	{
		return targetVelocity;
	}

	/** Returns this aircraft's target altitude */
	public float getTargetAltitude()
	{
		return targetAltitude;
	}

	/**
	 * Sets a new target velocity for this object
	 *
	 * <p>The object will rotate and change speed towards its new target velocity
	 *
	 * @param newVelocity new target velocity
	 */
	public void setTargetVelocity(Vector2D newVelocity)
	{
		this.targetVelocity = newVelocity;
	}

	/**
	 * Sets a new target altitude for this object
	 *
	 * <p>The object will start climbing / falling towards its target altitude
	 *
	 * @param newAltitude new target altitude
	 */
	public void setTargetAltitude(float newAltitude)
	{
		this.targetAltitude = newAltitude;
	}

	/** Moves value towards target in one step of size change */
	private static float floatMoveTowards(float value, float target, float change)
	{
		if (target < value)
		{
			value -= change;
			if (target > value)
				value = target;
		}
		else if (target > value)
		{
			value += change;
			if (target < value)
				value = target;
		}

		return value;
	}

	/** As floatMoveTowards, followed by clamping the result to maximum and minimum values */
	private static float floatMoveAndClamp(float value, float target, float change, float min, float max)
	{
		value = floatMoveTowards(value, target, change);

		if (value < min)
			value = min;
		if (value > max)
			value = max;

		return value;
	}

	/**
	 * Called every game tick to update this object's position and other data
	 *
	 * @param dt number of seconds elapsed since the last call to this method
	 * @see Airspace#refresh(float)
	 */
	public void refresh(float dt)
	{
		// Update altitude
		if (altitude != targetAltitude)
		{
			float maxAltitude = getMaxAltitude();
			float ascentAmount = getAscentRate() * dt;

			altitude = floatMoveAndClamp(altitude, targetAltitude, ascentAmount, 0, maxAltitude);
		}

		// Update velocity
		if (!velocity.equals(targetVelocity))
		{
			float minSpeed = getMinSpeed();
			float maxSpeed = getMaxSpeed();
			float acceleration = getMaxAcceleration() * dt;
			float turnRate = getMaxTurnRate() * dt;

			// Process speed value
			float speed = velocity.getLength();
			float targetSpeed = targetVelocity.getLength();

			speed = floatMoveAndClamp(speed, targetSpeed, acceleration, minSpeed, maxSpeed);

			// Process angle value
			float angle = velocity.getAngle();
			float targetAngle = targetVelocity.getAngle();

			angle = floatMoveTowards(angle, targetAngle, turnRate);

			// Reconstruct velocity vector
			velocity = Vector2D.fromPolar(speed, angle);
		}

		// Update position
		position = position.add(velocity.multiply(dt));
	}

	/**
	 * Draws this object
	 *
	 * @param state any state information to be passed to the drawer
	 */
	public abstract void draw(Object state);

	/** Returns true if this object is solid (can collide with anything) */
	public boolean isSolid()
	{
		return true;
	}

	/**
	 * Returns the size (radius) of this object
	 * <p>
	 * This is used to calculate collisions between objects.
	 */
	public abstract float getSize();

	/** Returns the rate of change of altitude (units / sec) */
	protected abstract float getAscentRate();

	/** Returns the minimum speed of the object */
	protected abstract float getMinSpeed();

	/** Returns the maximum speed of the object */
	protected abstract float getMaxSpeed();

	/** Returns the maximum altitude of the object */
	protected abstract float getMaxAltitude();

	/** Returns the maximum rate of change of speed (units / sec) */
	protected abstract float getMaxAcceleration();

	/** Returns the maximum rate of change of angle (radians / sec) */
	protected abstract float getMaxTurnRate();
}
