package seprhou.logic;

/**
 * Contains the configuration properties for an Airspace
 */
public class AirspaceConfig
{
	private final Rectangle dimensions;
	private final float horizontalSeparation, verticalSeparation;

	private final AircraftObjectFactory objectFactory;

	/**
	 * Constructs a new AirspaceConfig object
	 *
	 * @param objectFactory factory used to create aircraft
	 * @param dimensions dimensions of the game area
	 * @param horizontalSeparation horizontal separation distance
	 * @param verticalSeparation vertical separation distance
	 */
	public AirspaceConfig(AircraftObjectFactory objectFactory, Rectangle dimensions,
						  float horizontalSeparation, float verticalSeparation)
	{
		if (objectFactory == null)
			throw new IllegalArgumentException("objectFactory cannot be null");
		if (dimensions == null)
			throw new IllegalArgumentException("dimensions cannot be null");

		this.dimensions = dimensions;
		this.horizontalSeparation = horizontalSeparation;
		this.verticalSeparation = verticalSeparation;
		this.objectFactory = objectFactory;
	}

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
