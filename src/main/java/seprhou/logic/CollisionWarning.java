package seprhou.logic;

/**
 * A warning that two aircraft are "close" to each other
 */
public class CollisionWarning
{
	private final Aircraft aircraft1, aircraft2;

	/**
	 * Constructs a warning involving two aircraft
	 *
	 * @param aircraft1 the first aircraft
	 * @param aircraft2 the second aircraft
	 */
	public CollisionWarning(Aircraft aircraft1, Aircraft aircraft2)
	{
		this.aircraft1 = aircraft1;
		this.aircraft2 = aircraft2;
	}

	/** returns the first aircraft */
	public Aircraft getAircraft1()
	{
		return aircraft1;
	}

	/** returns the second aircraft */
	public Aircraft getAircraft2()
	{
		return aircraft2;
	}

	/** returns whether or not the aircraft have collided */
	public boolean hasCollided()
	{
		// TODO Implement this
		return false;
	}

	/** returns the horizontal distance between the aircraft */
	public float getHorizDistance()
	{
		// TODO Implement this
		return 0;
	}

	/** returns the vertical distance between the aircraft */
	public float getVertDistance()
	{
		// TODO Implement this
		return 0;
	}
}
