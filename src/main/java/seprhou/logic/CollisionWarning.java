package seprhou.logic;

/**
 * A warning that two aircraft are "close" to each other
 */
public class CollisionWarning
{
	private final AirspaceObject object1, object2;

	/**
	 * Constructs a warning involving two aircraft
	 *
	 * @param object1 the first aircraft
	 * @param object2 the second aircraft
	 */
	public CollisionWarning(AirspaceObject object1, AirspaceObject object2)
	{
		this.object1 = object1;
		this.object2 = object2;
	}

	/** returns the first aircraft */
	public AirspaceObject getObject1()
	{
		return object1;
	}

	/** returns the second aircraft */
	public AirspaceObject getObject2()
	{
		return object2;
	}

	/** returns whether or not the aircraft have collided */
	public boolean hasCollided()
	{
		float threshold = object1.getSize() + object2.getSize();
		return getLateralDistance() < threshold && getVerticalDistance() < threshold;
	}

	/** returns the horizontal distance between the objects */
	public float getLateralDistance()
	{
		return object1.getPosition().distanceTo(object2.getPosition());
	}

	/** returns the vertical distance between the objects (altitudes) */
	public float getVerticalDistance()
	{
		return Math.abs(object1.getAltitude() - object2.getAltitude());
	}
}
