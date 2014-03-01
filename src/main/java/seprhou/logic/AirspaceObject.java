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
	protected Vector2D targetVelocity = Vector2D.ZERO;
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
		// Apply speed clamps
		float maxSpeed = getMaxSpeed();
		float minSpeed = getMinSpeed();
		float speed = newVelocity.getLength();

		if (speed < minSpeed || speed > maxSpeed)
		{
			if (speed < minSpeed)
				speed = minSpeed;
			else if (speed > maxSpeed)
				speed = maxSpeed;

			this.targetVelocity = newVelocity.changeLength(speed);
		}
		else
		{
			this.targetVelocity = newVelocity;
		}
	}

	/**
	 * Sets a new target velocity for this object without range clamping
	 * 
	 * <p>
	 * The object will rotate and change speed towards its new target velocity
	 * 
	 * @param newVelocity new target velocity
	 */
	public void setTargetVelocityNoClamping(Vector2D newVelocity) {
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
		// Apply altitude clamps
		float minAltitude = getMinAltitude();
		float maxAltitude = getMaxAltitude();

		if (newAltitude < minAltitude)
			newAltitude = minAltitude;
		else if (newAltitude > maxAltitude)
			newAltitude = maxAltitude;

		this.targetAltitude = newAltitude;
	}

	/**
	 * Sets a new target altitude for this object without range clamping
	 * 
	 * <p>
	 * The object will start climbing / falling towards its target altitude
	 * 
	 * @param newAltitude new target altitude
	 */
	public void setTargetAltitudeNoClamping(float newAltitude) {
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
	
	public abstract void setViolated(boolean value);

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
			float ascentAmount = getAscentRate() * dt;

			altitude = floatMoveTowards(altitude, targetAltitude, ascentAmount);
		}

		// Update velocity
		if (!velocity.equals(targetVelocity))
		{
			float acceleration = getMaxAcceleration() * dt;
			float turnRate = getMaxTurnRate() * dt;

			// Process speed value
			float speed = velocity.getLength();
			float targetSpeed = targetVelocity.getLength();

			speed = floatMoveTowards(speed, targetSpeed, acceleration);

			// Process angle value
			float angle = velocity.getAngle();
			float targetAngle = targetVelocity.getAngle();

			// Adjust turn direction so the angle moves around the discontinuity properly
			if (Math.abs(angle - targetAngle) > Math.PI)
				turnRate = -turnRate;

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
	public abstract float getAscentRate();

	/** Returns the minimum speed of the object */
	public abstract float getMinSpeed();

	/** Returns the maximum speed of the object */
	public abstract float getMaxSpeed();

	/** Returns the minimum altitude of the object */
	public abstract float getMinAltitude();

	/** Returns the maximum altitude of the object */
	public abstract float getMaxAltitude();

	/** Returns the maximum rate of change of speed (units / sec) */
	public abstract float getMaxAcceleration();

	/** Returns the maximum rate of change of angle (radians / sec) */
	public abstract float getMaxTurnRate();

	/**
	 * Returns a check on whether an Object has completed it's flight plan or
	 * not
	 */
	public abstract boolean isFinished();

	/**
	 * Returns a check on whether an Object has violated an exclusion zone or
	 * not
	 */
	public abstract boolean isViolated();

	/** Returns the Score of the Object */
	public abstract int getScore();

	/** Returns the FLightPlan of an Object */
	public abstract FlightPlan getFlightPlan();

	/** Sets the flight plan of an Object */
	public abstract void setFlightPlan(FlightPlan flightPlan);

	/** Resets the details of a landed Plane */
	public abstract void resetRunwayPlane();
}
