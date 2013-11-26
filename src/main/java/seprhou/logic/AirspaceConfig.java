package seprhou.logic;

import java.util.List;

/**
 * Contains the configuration properties for an Airspace
 */
public class AirspaceConfig
{
	private Rectangle dimensions;
	private float horizontalSeparation, verticalSeparation;

	private List<Vector2D> waypoints, entryExitPoints;

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

	/** Returns the list of available intermediate waypoints */
	public List<Vector2D> getWaypoints()
	{
		return waypoints;
	}

	/** Returns the list of available entry and exit points */
	public List<Vector2D> getEntryExitPoints()
	{
		return entryExitPoints;
	}
}
