package seprhou.logic;

import java.util.Collections;
import java.util.List;

/**
 * The default flight path generator
 */
public class DefaultFlightPathGenerator
{
	private final List<Vector2D> waypoints, entryExitPoints;

	/**
	 * Creates a new flight path generator, choosing waypoints from the given list
	 *
	 * @param waypoints waypoint list
	 * @param entryExitPoints entry and exit point list
	 */
	public DefaultFlightPathGenerator(List<Vector2D> waypoints, List<Vector2D> entryExitPoints)
	{
		this.waypoints = Collections.unmodifiableList(waypoints);
		this.entryExitPoints = Collections.unmodifiableList(entryExitPoints);
	}

	/** Returns the list of waypoints */
	public List<Vector2D> getWaypoints()
	{
		return waypoints;
	}

	/** Returns the list of entry and exit points */
	public List<Vector2D> getEntryExitPoints()
	{
		return entryExitPoints;
	}

	/**
	 * Creates a new flightpath
	 *
	 * <p>
	 * This method makes no (and cannot make any) distinction between types of
	 * aircraft (all types get the same waypoint lists).
	 *
	 * @param airspace the airspace the aircraft will be created in
	 * @return a valid list of waypoints
	 */
	public List<Waypoint> makeFlightPath(Airspace airspace)
	{
		// TODO implement this
		return null;
	}
}
