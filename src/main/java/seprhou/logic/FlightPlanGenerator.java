package seprhou.logic;

import java.util.*;

/**
 * The default flight plan generator which controls introducing aircraft into the game
 *
 * <p>
 * This class contains many configuration parameters affecting generated flight paths.
 * To use this class properly, call the setter for all the options which you want to
 * change after creating this class.
 */
public class FlightPlanGenerator
{
	private final Random random = new Random();

	// Config options
	//  Filled with some "sensibleish" default options
	private List<Vector2D> waypoints;
	private List<Vector2D> entryExitPoints;
	private List<Integer> initialAltitudes = Arrays.asList(30000, 35000, 40000);
	private List<Integer> initialSpeeds = Arrays.asList(50);
	private float minSafeEntryDistance = 200;
	private float aircraftPerSec = 0.2f;
	private int maxAircraft = 5;
	private int minWaypoints = 2;
	private int maxWaypoints = 4;

	/** Sets the list of waypoints to choose from */
	public void setWaypoints(List<Vector2D> waypoints) { this.waypoints = waypoints; }

	/** Sets the list of entry and exit points to choose from */
	public void setEntryExitPoints(List<Vector2D> entryExitPoints) { this.entryExitPoints = entryExitPoints; }

	/** Sets the list of initial altitudes */
	public void setInitialAltitudes(List<Integer> initialAltitudes) { this.initialAltitudes = initialAltitudes; }

	/** Sets the list of initial speeds */
	public void setInitialSpeeds(List<Integer> initialSpeeds) { this.initialSpeeds = initialSpeeds; }

	/** Sets the minimum free radius needed for an aircraft to enter at an entry point */
	public void setMinSafeEntryDistance(float minSafeEntryDistance) { this.minSafeEntryDistance = minSafeEntryDistance; }

	/** Sets the average number of aircraft to generate per second */
	public void setAircraftPerSec(float aircraftPerSec) { this.aircraftPerSec = aircraftPerSec; }

	/** Sets the maximum number of aircraft */
	public void setMaxAircraft(int maxAircraft) { this.maxAircraft = maxAircraft; }

	/** Sets the minimum number of waypoints in a flight plan */
	public void setMinWaypoints(int minWaypoints) { this.minWaypoints = minWaypoints; }

	/** Sets the maximum number of waypoints in a flight plan */
	public void setMaxWaypoints(int maxWaypoints) { this.maxWaypoints = maxWaypoints; }

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
	 * Chooses an item from a list
	 *
	 * @param list list to choose from
	 * @param <T> type of the items in the list
	 * @return the chosen item
	 */
	private <T> T randomItem(List<T> list)
	{
		return list.get(random.nextInt(list.size()));
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
				if (object.isSolid() && object.getPosition().distanceTo(point) < minSafeEntryDistance)
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
		// Sanity check at least the basic options
		if (waypoints == null || entryExitPoints == null)
		{
			throw new IllegalStateException("FlightPlanGenerator has not been setup correctly\n" +
					"  You must call at least setWaypoints and setEntryExitPoints on it");
		}

		// Generate a subset of entryExitPoints which contains the points which we can enter from
		List<Vector2D> entryPointSubset = generateEntryPointSubset(airspace);
		if (entryPointSubset.size() == 0)
			return null;

		// Choose some waypoints + 2 entry and exit points
		int waypointCount = random.nextInt(maxWaypoints - minWaypoints) + minWaypoints;
		List<Vector2D> myWaypoints = randomSubset(waypoints, waypointCount);
		Vector2D entryPoint = randomItem(entryPointSubset);
		Vector2D exitPoint = randomItem(entryExitPoints, entryPoint);

		// Insert entry + exit points into the list
		myWaypoints.add(0, entryPoint);
		myWaypoints.add(exitPoint);

		// Choose initial speed and altitude
		float initialSpeed = randomItem(initialSpeeds);
		float initialAltitude = randomItem(initialAltitudes);

		// Create flight plan
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
		// Check max aircraft
		if (airspace.getActiveObjects().size() >= maxAircraft)
			return null;

		// Add some randomness
		if (random.nextFloat() >= delta * aircraftPerSec)
			return null;

		// Try to generate an
		return makeFlightPlanNow(airspace);
	}
}
