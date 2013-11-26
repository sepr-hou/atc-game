package seprhou.logic;

import java.util.List;

/**
 * Implementation of AircraftFactory providing some common routines
 *
 * <p>
 * When {@link #makeObject(Airspace, float)} is called, you should call
 * {@link #shouldMakeObject(Airspace, float)} to determine whether to create
 * an object this refresh. If this method returns true, you can create
 * a new object which will enter the airspace.
 *
 * <p>
 * While creating the object, you can call {@link #makeWaypointList(Airspace)}
 * to call the default flight plan generator to help in creating aircraft.
 *
 * @see AircraftObjectFactory
 */
public abstract class AbstractAircraftObjectFactory implements AircraftObjectFactory
{
	private float ratePerSec;
	private int maxAircraft;

	@Override
	public AirspaceObject makeObject(Airspace airspace, float delta)
	{
		// TODO implement this
		return null;
	}

	/**
	 * Called to determine if an object should be created this refresh
	 *
	 * @param airspace the airspace the object will be created in
	 * @param delta time (in seconds) since last game refresh
	 * @return {@code true} to create an object, {@code false} to not
	 */
	protected boolean shouldMakeObject(Airspace airspace, float delta)
	{
		// TODO implement this
		return false;
	}

	/**
	 * Utility method which creates a list of waypoints for an Airspace
	 *
	 * <p>
	 * This is the default implementation of the flight path generator.
	 * This can be called within the {@link #makeObject(Airspace, float)} method to
	 * generate a valid list of waypoints.
	 *
	 * <p>
	 * This method makes no (and cannot make any) distinction between types of
	 * aircraft (all types get the same waypoint lists).
	 *
	 * @param airspace the airspace the aircraft will be created in
	 * @return a valid list of waypoints
	 */
	public static List<Waypoint> makeWaypointList(Airspace airspace)
	{
		// TODO implement this
		return null;
	}
}
