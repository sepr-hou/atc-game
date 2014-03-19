package seprhou.logic;

import java.util.ArrayList;
import java.util.List;

import static seprhou.logic.LogicConstants.*;

/**
 * The default flight plan generator which controls introducing aircraft into the game
 */
public class FlightPlanGenerator
{
	private int nextTakeOffRunway = 0;

	// State (time since last aircraft)
	private float timeSinceLastAircraft = Float.POSITIVE_INFINITY;

	/**
	 * Generates the list of points which a flight can safely enter currently
	 *
	 * @param airspace airspace to gather data from
	 * @return the list of safe entry points
	 */
	private List<Vector2D> generateEntryPointSubset(Airspace airspace)
	{
		List<Vector2D> safePoints = new ArrayList<>();

		for (Vector2D point : ENTRY_EXIT_POINTS)
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
	 * @param canLand if true, the aircraft is permitted to land (but it might not)
	 * @param isOnRunway if true, the aircraft starts on the runway (taking off)
	 * @return the new flight plan or null if no aircraft should be created now
	 * @see #makeFlightPlan(Airspace, float)
	 */
	public FlightPlan makeFlightPlanNow(Airspace airspace, boolean canLand, boolean isOnRunway)
	{
		// Generate a subset of entryExitPoints which contains the points which we can enter from
		List<Vector2D> entryPointSubset = generateEntryPointSubset(airspace);
		if (entryPointSubset.size() == 0)
			return null;

		// Choose some waypoints + an entry and exit point, also determines
		// whether a plane is landing or not.

		boolean landing;
		Vector2D entryPoint = Utils.randomItem(entryPointSubset);
		List<Vector2D> myWaypoints = new ArrayList<Vector2D>();
		
		
		//Creates list of eligible first waypoints and chooses a random one to be added
		List<Vector2D> firstWaypoints = new ArrayList<Vector2D>();
		for (int i=0; i<16; i++){
			if (entryPoint.distanceTo(WAYPOINTS.get(i)) < 400){
				firstWaypoints.add(WAYPOINTS.get(i));
			}
		}
		Vector2D firstWaypoint = Utils.randomItem(firstWaypoints);
		myWaypoints.add(firstWaypoint);
		
		Vector2D currentWaypoint = firstWaypoint;
		int flightPlanLength = Utils.getRandom().nextInt(MAX_WAYPOINTS - MIN_WAYPOINTS) + MIN_WAYPOINTS;
		
		for (int i = 0; i < flightPlanLength; i++){
			List<Vector2D> nextWaypoints = new ArrayList<Vector2D>();
			for (int j=0; j<16; j++){
				Vector2D newWaypoint = WAYPOINTS.get(j);
				if ((currentWaypoint.distanceTo(newWaypoint)) < 600)
				{
					System.out.println("Waypoint eligable");
					if (!myWaypoints.contains(newWaypoint)){
						nextWaypoints.add(newWaypoint);
						System.out.println("Waypoint chosen");
						currentWaypoint = newWaypoint;
					}
				}
			}
			myWaypoints.add(Utils.randomItem(nextWaypoints));
		}
		
		boolean startOnRunway = isOnRunway;

		/* Take off from runway instead. */
		if (startOnRunway) {
			// Makes sure that aircrafts take off from alternating runways.
			Runway runway = RUNWAYS.get(this.nextTakeOffRunway);
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
			Runway landingStrip = Utils.randomItem(RUNWAYS);
			Vector2D landingPoint = landingStrip.getEnd();
			Vector2D exitPoint = landingStrip.getStart();
			myWaypoints.add(landingPoint);
			myWaypoints.add(exitPoint);
			landing = true;
		} else {
			Vector2D exitPoint = Utils.randomItem(ENTRY_EXIT_POINTS, entryPoint);
			myWaypoints.add(exitPoint);
			landing = false;
		}

		// Choose initial speed and altitude
		float initialSpeed = Utils.randomItem(INITIAL_SPEEDS);
		float initialAltitude = Utils.randomItem(INITIAL_ALTITUDES);

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
		if (timeSinceLastAircraft < MIN_TIME_BETWEEN_AIRCRAFT)
			return null;

		// Check max aircraft
		if (airspace.getActiveObjects().size() >= MAX_AIRCRAFT)
			return null;

		// Add some randomness
		if (Utils.getRandom().nextFloat() >= delta * AIRCRAFT_PER_SEC)
			return null;

		// Try to generate an
		return makeFlightPlanNow(airspace);
	}
}
