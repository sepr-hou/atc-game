package seprhou.logic;

/**
 * Contains the configuration properties for an Airspace
 */
public class AirspaceConfig
{
	private Rectangle dimensions;
	private float horizontalSeparation, verticalSeparation;

	private AircraftObjectFactory objectFactory;

	/** Returns the factory responsible for constructing airspace objects */
	public AircraftObjectFactory getObjectFactory()
	{
		return objectFactory;
	}

	/** Returns the dimensions of the airspace */
	public Rectangle getDimensions()
	{
		return dimensions;
	}

	/**
	 * Returns the horizontal separation distance to generate warnings at
	 *
	 * <p>This only affects the collision warnings. Actual crashes are determined from the aircraft sizes
	 *
	 * @see AirspaceObject#getSize()
	 */
	public float getHorizontalSeparation()
	{
		return horizontalSeparation;
	}

	/**
	 * Returns the vertical separation distance to generate warnings at
	 *
	 * <p>This only affects the collision warnings. Actual crashes are determined from the aircraft sizes
	 *
	 * @see AirspaceObject#getSize()
	 */
	public float getVerticalSeparation()
	{
		return verticalSeparation;
	}
}
