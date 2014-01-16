package seprhou.logic;

import java.util.*;

/**
 * The default flight plan generator which controls introducing aircraft into the game
 */
public class FlightPlanGenerator
{
	// TODO Remove or turn into constructor argument
	private static final float RATE_PER_SEC = 0.2f;
	private static final float MIN_TIME = 1f;
	private static final int MAX_AIRCRAFT = 5;

	private static final int SPEED = 20;
	private static final List<Integer> ALTITUDES = Arrays.asList(30000, 35000, 40000);

	private static final int MIN_WAYPOINTS = 2;
	private static final int MAX_WAYPOINTS = 4;

	private static final float MIN_SAFE_ENTRY_DISTANCE = 200;

	private final List<Vector2D> waypoints, entryExitPoints;
	private final Random random = new Random();
	private float timeSinceLastAircraft;

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
	 * Chooses an item from a list but does not include the invalidItem
	 *
	 * @param list list to choose from
	 * @param invalidItem the item which will not be picked
	 * @param <T> type of the items in the list
	 * @return the chosen item
	 */
	private <T> T randomItem(List<T> list, T invalidItem)
	{
		// Check for impossible situation
		if (list.size() == 1 && list.get(0) == invalidItem)
			throw new IllegalStateException("list given to randomItem contains no valid items!");

		// Choose one item and skip it if it's invalid
		int item = random.nextInt(list.size());
		if (list.get(item) == invalidItem)
			item = (item + 1) % list.size();

		return list.get(item);
	}

	/**
	 * Generates the list of points which a flight can safely enter currently
	 *
	 * @param airspace airspace to gather data from
	 * @return the list of safe entry points
	 */
	private List<Vector2D> generateEntryPointSubset(Airspace airspace)
	{
		List<Vector2D> safePoints = new ArrayList<>();

		for (Vector2D point : entryExitPoints)
		{
			boolean ok = true;

			// Test all aircraft against this point
			for (AirspaceObject object : airspace.getActiveObjects())
			{
				if (object.isSolid() && object.getPosition().distanceTo(point) < MIN_SAFE_ENTRY_DISTANCE)
				{
					ok = false;
					break;
				}
			}

			if (ok)
				safePoints.add(point);
		}

		return safePoints;
	}

	/**
	 * Creates a new flight plan immediately (without taking timers into account)
	 *
	 * <p>
	 * This is like {@link #makeFlightPlan(Airspace, float)} but will try to make one now even
	 * if the configuration doesn't want to now. This may still return null however if
	 * it is not even possible to generate a path without impossible to avoid collisions.
	 *
	 * @param airspace the airspace the aircraft will be created in
	 * @return the new flight plan or null if no aircraft should be created now
	 * @see #makeFlightPlan(Airspace, float)
	 */
	public FlightPlan makeFlightPlanNow(Airspace airspace)
	{
		// Generate a subset of entryExitPoints which contains the points which we can enter from
		List<Vector2D> entryPointSubset = generateEntryPointSubset(airspace);
		if (entryPointSubset.size() == 0)
			return null;

		// Choose some waypoints + 2 entry and exit points
		int waypointCount = random.nextInt(MAX_WAYPOINTS - MIN_WAYPOINTS) + MIN_WAYPOINTS;
		List<Vector2D> myWaypoints = randomSubset(waypoints, waypointCount);
		Vector2D entryPoint = randomItem(entryPointSubset, null);
		Vector2D exitPoint = randomItem(entryExitPoints, entryPoint);

		// Insert entry + exit points into the list
		myWaypoints.add(0, entryPoint);
		myWaypoints.add(exitPoint);

		// Choose initial speed and altitude
		float initialSpeed = SPEED;
		float initialAltitude = ALTITUDES.get(random.nextInt(ALTITUDES.size()));

		// Create flight plan
		timeSinceLastAircraft = 0;
		return new FlightPlan(myWaypoints, initialSpeed, initialAltitude);
	}

	/**
	 * Creates a new flight plan
	 *
	 * <p>
	 * This method uses all its various configuration settings to create a
	 * sutible flight plan. It does not generate a flight plan every time it
	 * is called however.
	 *
	 * @param airspace the airspace the aircraft will be created in
	 * @return the new flight plan or null if no aircraft should be created now
	 */
	public FlightPlan makeFlightPlan(Airspace airspace, float delta)
	{
		timeSinceLastAircraft += delta;

		// Check max aircraft
		if (airspace.getActiveObjects().size() >= MAX_AIRCRAFT)
			return null;

		// Check Minimum time between aircraft
		if (timeSinceLastAircraft < MIN_TIME)
			return null;

		// Add some randomness
		if (random.nextFloat() >= delta * RATE_PER_SEC)
			return null;

		// Try to generate an
		return makeFlightPlanNow(airspace);
	}
}
