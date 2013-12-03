package seprhou.logic;

import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * The default flight path generator
 */
public class DefaultFlightPathGenerator
{
	private final List<Vector2D> waypoints, entryExitPoints;
	private final Random random = new Random();

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
		List<Waypoint> flightPath = null;
		/*Creates entry waypoint and adds it to list*/
		float entrySpeed = 10;
		float entryAltitude = 10;
		int entryPointNumber = random.nextInt(entryExitPoints.size());
		Vector2D entryPointPosition = entryExitPoints.get(entryPointNumber);
		Waypoint entryPoint = new Waypoint(entryPointPosition, entrySpeed, entryAltitude);
		flightPath.add(entryPoint);
		
		/*Adds a random amount (0-5) of sensible waypoints to the list*/
		/*change*/float waypointSpeed = 8;
		/*change*/float waypointAltitude = 8;
		int numberOfWaypoints = random.nextInt(5);
		for (int i = 0; i < numberOfWaypoints; i++){
			Waypoint previousWaypoint = flightPath.get(i-1);
			Waypoint currentWaypoint = flightPath.get(i);
			Vector2D bestPosition = null;
			float bestDistance = Float.POSITIVE_INFINITY;
			for (Vector2D adjPosition : waypoints){
				float distance = adjPosition.distanceTo(currentWaypoint.getPosition());
				if (distance < bestDistance && adjPosition != previousWaypoint.getPosition()){
					bestDistance = distance;
					bestPosition = adjPosition;
				}
			}
			Waypoint newWaypoint = new Waypoint(bestPosition, waypointSpeed, waypointAltitude);
			flightPath.add(newWaypoint);
		}
		
		/*Creates exit waypoint and adds to list*/
		/*change*/float exitSpeed = 10;
		/*change*/float exitAltitude = 1;
		Waypoint currentWaypoint = flightPath.get(numberOfWaypoints - 1);
		Vector2D bestExit = null;
		float bestDistance = Float.POSITIVE_INFINITY;
		for (Vector2D exit : entryExitPoints){
			float distance = exit.distanceTo(currentWaypoint.getPosition());
			if (distance < bestDistance && exit != flightPath.get(0).getPosition()){
				bestDistance = distance;
				bestExit = exit;
			}
		}
		Waypoint exitPoint = new Waypoint(bestExit, exitSpeed, exitAltitude);
		flightPath.add(exitPoint);
		
		return flightPath;
	}
}
