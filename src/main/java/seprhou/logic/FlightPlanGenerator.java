package seprhou.logic;

import java.util.*;

/**
 * The default flight plan generator
 */
public class FlightPlanGenerator
{
	// TODO Remove or turn into constructor argument
	private static final int SPEED = 20;
	private static final List<Integer> ALTITUDES = Arrays.asList(30000, 35000, 40000);

	private static final int MIN_WAYPOINTS = 2;
	private static final int MAX_WAYPOINTS = 4;

	private final List<Vector2D> waypoints, entryExitPoints;
	private final Random random = new Random();

	/**
	 * Creates a new flight plan generator, choosing waypoints from the given list
	 *
	 * @param waypoints waypoint list
	 * @param entryExitPoints entry and exit point list
	 */
	public FlightPlanGenerator(List<Vector2D> waypoints, List<Vector2D> entryExitPoints)
	{
		if (waypoints.size() < MAX_WAYPOINTS)
			throw new IllegalArgumentException("size of waypoints must be at least " + MAX_WAYPOINTS);

		this.waypoints = Collections.unmodifiableList(waypoints);
		this.entryExitPoints = Collections.unmodifiableList(entryExitPoints);
	}

	/** Returns the list of waypoints */
	public List<Vector2D> getWaypoints() { return waypoints; }

	/** Returns the list of entry and exit points */
	public List<Vector2D> getEntryExitPoints()
	{
		return entryExitPoints;
	}

	/**
	 * Generates a random subset of the given list
	 *
	 * @param list list of items
	 * @param n number of items to choose
	 * @param <T> type of the items in the list
	 * @return the chosen item
	 */
	private <T> List<T> randomSubset(List<T> list, int n)
	{
		// Copy the list and shuffle it
		List<T> result = new ArrayList<>(list);
		Collections.shuffle(result, random);

		// Return first n items
		return result.subList(0, n);
	}

	/**
	 * Creates a new flight plan
	 *
	 * <p>
	 * This method makes no (and cannot make any) distinction between types of
	 * aircraft (all types get the same waypoint lists).
	 *
	 * @param airspace the airspace the aircraft will be created in
	 * @return a valid list of waypoints
	 */
	public FlightPlan makeFlightPlan(Airspace airspace)
	{
		// Choose some waypoints + 2 entry and exit points
		int waypointCount = random.nextInt(MAX_WAYPOINTS - MIN_WAYPOINTS) + MIN_WAYPOINTS;
		List<Vector2D> myEntryExitPoints = randomSubset(entryExitPoints, 2);
		List<Vector2D> myWaypoints = randomSubset(waypoints, waypointCount);

		// Concatenate the lists
		myWaypoints.add(0, myEntryExitPoints.get(0));
		myWaypoints.add(myEntryExitPoints.get(1));

		// Choose initial speed and altitude
		float initialSpeed = SPEED;
		float initialAltitude = ALTITUDES.get(random.nextInt(ALTITUDES.size()));

		// Create flight plan
		return new FlightPlan(myWaypoints, initialSpeed, initialAltitude);
	}
}
