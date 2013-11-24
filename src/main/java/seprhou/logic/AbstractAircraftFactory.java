package seprhou.logic;

import java.util.List;

/**
 * Implementation of AircraftFactory providing some common routines
 *
 * <p>
 * This class does the following when {@link #makeAircraft(Airspace, float)} is called
 * <ul>
 *   <li>{@link #shouldMakeAircraft(Airspace, float)} is called to determine if an aircraft
 *       should be created</li>
 *   <li>If the result is {@code false}, the method returns immediately</li>
 *   <li>Otherwise, {@link #makeFlightPlan(Airspace)} is called to create a {@link FlightPlan} object</li>
 *   <ul>
 *     <li>Generally, this method will chose an {@link AircraftType} to use and possibly call
 *         {@link #makeWaypointList(Airspace)} to generate a waypoint list</li>
 *   </ul>
 *   <li>Finally, the method will create an {@link Aircraft} objec to return to the caller</li>
 * </ul>
 *
 * @see AircraftFactory
 */
public abstract class AbstractAircraftFactory implements AircraftFactory
{
	private float ratePerSec;
	private int maxAircraft;

	@Override
	public Aircraft makeAircraft(Airspace airspace, float delta)
	{
		// TODO implement this
		return null;
	}

	/**
	 * Called to determine if an aircraft should be created this refresh
	 *
	 * @param airspace the airspace the aircraft will be created in
	 * @param delta time (in seconds) since last game refresh
	 * @return {@code true} to create an aircraft, {@code false} to not
	 */
	protected boolean shouldMakeAircraft(Airspace airspace, float delta)
	{
		// TODO implement this
		return false;
	}

	// TODO these methods may need changing depending on exactly what a FlightPlan represents

	/**
	 * Called to create a flight plan for an aircraft
	 *
	 * <p>
	 * This method allows you to customize what types of flights are generated
	 * by the aircraft generator.
	 *
	 * @param airspace the airspace the aircraft will be created in
	 * @return a valid aircraft flight plan
	 */
	protected abstract FlightPlan makeFlightPlan(Airspace airspace);

	/**
	 * Utility method which creates a list of waypoints for an Airspace
	 *
	 * <p>
	 * This is the default implementation of the flight path generator.
	 * This can be called within the {@link #makeFlightPlan(Airspace)} method to
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
