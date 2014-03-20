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

		// Choose an entry and exit point then some sensible waypoints between them,
		//  also determines whether a plane is landing or not.

		List<Vector2D> myWaypoints = new ArrayList<Vector2D>();

		if (isOnRunway) {
			// Makes sure that aircraft take off from alternating runways.
			Runway runway = RUNWAYS.get(this.nextTakeOffRunway);
			if (this.nextTakeOffRunway == 1)
				this.nextTakeOffRunway--;
			else
				this.nextTakeOffRunway++;
			myWaypoints.add(runway.getStart());
			myWaypoints.add(runway.getEnd());
		} else {
			// Choose random entry point and add into the list
			// if the plane is not taking off from the runway
			Vector2D entryPoint = Utils.randomItem(entryPointSubset);
			myWaypoints.add(entryPoint);
		}

		Vector2D entryPoint = myWaypoints.get(0);

		// If the plane has not started on runway,
		// Randomly picks whether the plane should land based on Landing Chance constant
		// Or leave through exit point.
		Vector2D landingPoint = null;
		Vector2D exitPoint;
		boolean landing;
		if (Utils.getRandom().nextInt(LANDING_CHANCE) != 1 && canLand && !isOnRunway) {
			Runway landingStrip = Utils.randomItem(RUNWAYS);
			landingPoint = landingStrip.getEnd();
			exitPoint = landingStrip.getStart();
			landing = true;
		} else {
			exitPoint = Utils.randomItem(ENTRY_EXIT_POINTS, entryPoint);
			landing = false;
		}


		// Create list of eligible first waypoints and chooses a random one to be added
		List<Vector2D> firstWaypoints = new ArrayList<Vector2D>();
		for (int i=0; i<WAYPOINTS.size(); i++){
			if (entryPoint.distanceTo(WAYPOINTS.get(i)) < 400){
				firstWaypoints.add(WAYPOINTS.get(i));
			}
		}
		Vector2D firstWaypoint = Utils.randomItem(firstWaypoints);
		myWaypoints.add(firstWaypoint);
		Vector2D currentWaypoint = firstWaypoint;

		// Choose a random flightplan length between Min and Max constants
		int flightPlanLength = Utils.getRandom().nextInt(MAX_WAYPOINTS - MIN_WAYPOINTS) + MIN_WAYPOINTS;

		// Add flightplanlength number of waypoints to the flightplan
		for (int i = 0; i < flightPlanLength; i++){
			// List of eligible waypoints
			List<Vector2D> nextWaypoints = new ArrayList<Vector2D>();
			// Check each waypoint to see if eligible and add to list
			for (int j=0; j < WAYPOINTS.size(); j++){
				Vector2D newWaypoint = WAYPOINTS.get(j);
				// Check each waypoint to see whether it is closer to the exit/landing point than the previous
				// waypoint in the flightplan
				if (landing){
					if ((newWaypoint.distanceTo(landingPoint)) < (currentWaypoint.distanceTo(landingPoint)))
					{
						if (!myWaypoints.contains(newWaypoint))
						{
							nextWaypoints.add(newWaypoint);
						}
					}
				} else {
					if ((newWaypoint.distanceTo(exitPoint)) < (currentWaypoint.distanceTo(exitPoint)))
					{
						if (!myWaypoints.contains(newWaypoint))
						{
							nextWaypoints.add(newWaypoint);
						}
					}
				}
			}
			// Choose random waypoint from list of eligibles and add to flightplan
			if (nextWaypoints.size() != 0){
				Vector2D waypointAdded = Utils.randomItem(nextWaypoints);
				myWaypoints.add(waypointAdded);
				currentWaypoint = waypointAdded;
			}
		}

		// Add the exitpoint(s) to flightplan.
		if (landing){
			myWaypoints.add(landingPoint);
			myWaypoints.add(exitPoint);
		} else {
			myWaypoints.add(exitPoint);
		}

		// Choose initial speed and altitude
		float initialSpeed = Utils.randomItem(INITIAL_SPEEDS);
		float initialAltitude = Utils.randomItem(INITIAL_ALTITUDES);

		// Create flight plan
		timeSinceLastAircraft = 0;
		return new FlightPlan(myWaypoints, initialSpeed, initialAltitude, landing, isOnRunway);
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
