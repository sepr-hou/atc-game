package seprhou.logic;

import java.util.ArrayList;
import java.util.List;

import seprhou.gui.Constants;

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
	// Config options
	//  Filled with some "sensibleish" default options
	private List<Vector2D> waypoints;
	private List<Vector2D> entryExitPoints;
	private List<Runway> runways;
	private List<Float> initialAltitudes = Constants.INITIAL_ALTITUDES;
	private List<Float> initialSpeeds = Constants.INITIAL_SPEEDS;
	private float minSafeEntryDistance = Constants.MIN_SAFE_ENTRY_DISTANCE;
	private float minTimeBetweenAircraft = Constants.MIN_TIME_BETWEEN_AIRCRAFT;
	private float aircraftPerSec = Constants.AIRCRAFT_PER_SEC;
	private int maxAircraft = Constants.MAX_AIRCRAFT;
	private int minWaypoints = Constants.MIN_WAYPOINTS;
	private int maxWaypoints = Constants.MAX_WAYPOINTS;
	private int nextTakeOffRunway = 0;

	// State (time since last aircraft)
	private float timeSinceLastAircraft = Float.POSITIVE_INFINITY;

	/** Sets the list of waypoints to choose from */
	public void setWaypoints(List<Vector2D> waypoints) { this.waypoints = waypoints; }

	/** Sets the list of entry and exit points to choose from */
	public void setEntryExitPoints(List<Vector2D> entryExitPoints) { this.entryExitPoints = entryExitPoints; }

	/** Sets the list of initial altitudes */
	public void setInitialAltitudes(List<Float> initialAltitudes) { this.initialAltitudes = initialAltitudes; }

	/** Sets the list of initial speeds */
	public void setInitialSpeeds(List<Float> initialSpeeds) { this.initialSpeeds = initialSpeeds; }

	/** Sets the minimum free radius needed for an aircraft to enter at an entry point */
	public void setMinSafeEntryDistance(float minSafeEntryDistance) { this.minSafeEntryDistance = minSafeEntryDistance; }

	/** Sets the minimum number of seconds between aircraft being generated */
	public void setMinTimeBetweenAircraft(float minTimeBetweenAircraft) { this.minTimeBetweenAircraft = minTimeBetweenAircraft; }

	/** Sets the average number of aircraft to generate per second */
	public void setAircraftPerSec(float aircraftPerSec) { this.aircraftPerSec = aircraftPerSec; }

	/** Sets the maximum number of aircraft */
	public void setMaxAircraft(int maxAircraft) { this.maxAircraft = maxAircraft; }

	/** Sets the minimum number of waypoints in a flight plan */
	public void setMinWaypoints(int minWaypoints) { this.minWaypoints = minWaypoints; }

	/** Sets the maximum number of waypoints in a flight plan */
	public void setMaxWaypoints(int maxWaypoints) { this.maxWaypoints = maxWaypoints; }

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

	public FlightPlan makeFlightPlanNow(Airspace airspace) {
		return this.makeFlightPlanNow(airspace, true, false);
	}

	public FlightPlan makeFlightPlanNow(Airspace airspace, boolean canLand) {
		return this.makeFlightPlanNow(airspace, canLand, false);
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
	 * @param canLand
	 * @return the new flight plan or null if no aircraft should be created now
	 * @see #makeFlightPlan(Airspace, float)
	 */
	public FlightPlan makeFlightPlanNow(Airspace airspace, boolean canLand, boolean isOnRunway)
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

		// Choose some waypoints + 2 entry and exit points, also determines
		// whether a plane is landing or not.
		
		// CHANGE THIS TO DO #25

		boolean landing;
		Vector2D entryPoint = Utils.randomItem(entryPointSubset);
		//int waypointCount = Utils.getRandom().nextInt(maxWaypoints - minWaypoints) + minWaypoints;
		//List<Vector2D> myWaypoints = Utils.randomSubset(waypoints, waypointCount);
		List<Vector2D> myWaypoints = new ArrayList<Vector2D>();
		List<Vector2D> firstWaypoints = new ArrayList<Vector2D>();
		for (int i=0; i<16; i++){
			if (entryPoint.distanceTo(waypoints.get(i)) < 400){
				firstWaypoints.add(waypoints.get(i));
			}
		}
		Vector2D firstWaypoint = Utils.randomItem(firstWaypoints);
		myWaypoints.add(firstWaypoint);
		
		Vector2D currentWaypoint = firstWaypoint;

		for (int i=0; i<16; i++){
			Vector2D newWaypoint = waypoints.get(i);
			if ((currentWaypoint.distanceTo(newWaypoint)) < 600)
			{
				if (!waypoints.contains(newWaypoint)){
					myWaypoints.add(newWaypoint);
					currentWaypoint = newWaypoint;
				}

			}
		}
		
		boolean startOnRunway = isOnRunway;

		/* Take off from runway instead. */
		if (startOnRunway) {
			if (Constants.DEBUG)
				System.out.println("Starts on runway");
			// Makes sure that aircrafts take off from alternating runways.
			Runway runway = Constants.RUNWAYS.get(this.nextTakeOffRunway);
			if (this.nextTakeOffRunway == 1)
				this.nextTakeOffRunway--;
			else
				this.nextTakeOffRunway++;
			myWaypoints.add(0, runway.getStart());
			myWaypoints.add(1, runway.getEnd());
		} else {
			// Insert entry point into the list
			// if the plane is not taking off from the runway
			myWaypoints.add(0, entryPoint);
		}
		// If the plane has not started on runway,
		// Randomly picks whether the plane should land
		// Or leave through exit point.
		if (Utils.getRandom().nextInt(2) != 1 && canLand && !startOnRunway) {
			Runway landingStrip = Utils.randomItem(this.runways);
			Vector2D landingPoint = landingStrip.getEnd();
			Vector2D exitPoint = landingStrip.getStart();
			myWaypoints.add(landingPoint);
			myWaypoints.add(exitPoint);
			landing = true;
		} else {
			Vector2D exitPoint = Utils.randomItem(entryExitPoints, entryPoint);
			myWaypoints.add(exitPoint);
			landing = false;
		}

		// Choose initial speed and altitude
		float initialSpeed = Utils.randomItem(initialSpeeds);
		float initialAltitude = Utils.randomItem(initialAltitudes);

		// Create flight plan
		timeSinceLastAircraft = 0;
		return new FlightPlan(myWaypoints, initialSpeed, initialAltitude, landing, startOnRunway);
	}

	/**
	 * Creates a new flight plan
	 *
	 * <p>
	 * This method uses all its various configuration settings to create a
	 * suitable flight plan. It does not generate a flight plan every time it
	 * is called however.
	 *
	 * @param airspace the airspace the aircraft will be created in
	 * @return the new flight plan or null if no aircraft should be created now
	 */
	public FlightPlan makeFlightPlan(Airspace airspace, float delta)
	{
		timeSinceLastAircraft += delta;

		// Check time since last aircraft
		if (timeSinceLastAircraft < minTimeBetweenAircraft)
			return null;

		// Check max aircraft
		if (airspace.getActiveObjects().size() >= maxAircraft)
			return null;

		// Add some randomness
		if (Utils.getRandom().nextFloat() >= delta * aircraftPerSec)
			return null;

		// Try to generate an
		return makeFlightPlanNow(airspace);
	}

	public List<Runway> getRunways() {
		return this.runways;
	}

	public void setRunways(List<Runway> runways) {
		this.runways = runways;
	}
}
