package seprhou.logic;

import java.util.Collections;
import java.util.List;

/**
 * An aircraft in the airspace
 */
public abstract class Aircraft extends AirspaceObject
{
	private final String name;
	private final float weight;
	private final int crew;

	private final List<Waypoint> flightPlan;

	private int lastWaypoint;
	private int waypointsHit;

	/**
	 * Constructs a new aircraft
	 *
	 * @param name aircraft name (descriptive only)
	 * @param weight aircraft weight
	 * @param crew number of crew
	 * @param flightPlan aircraft flight plan (must not be modified)
	 */
	protected Aircraft(String name, float weight, int crew, List<Waypoint> flightPlan)
	{
		if (flightPlan == null)
			throw new IllegalArgumentException("flightPlan cannot be null");
		if (flightPlan.size() < 2)
			throw new IllegalArgumentException("flightPlan must have at least 2 waypoints");

		if (name == null)
			name = "";

		// Setup my attributes
		this.name = name;
		this.weight = weight;
		this.crew = crew;
		this.flightPlan = Collections.unmodifiableList(flightPlan);

		// Setup initial object attributes
		Waypoint waypoint1 = flightPlan.get(0);
		Waypoint waypoint2 = flightPlan.get(1);

		Vector2D direction = waypoint2.getPosition().sub(waypoint1.getPosition());

		this.position = waypoint1.getPosition();
		this.velocity = direction.changeLength(waypoint2.getSpeed());
		this.altitude = waypoint2.getAltitude();

		this.targetVelocity = this.velocity;
		this.targetAltitude = this.altitude;
	}

	/** Returns this aircraft's name */
	public String getName()
	{
		return name;
	}

	/** Returns this aircraft's weight */
	public float getWeight()
	{
		return weight;
	}

	/** Returns this aircraft's crew */
	public int getCrew()
	{
		return crew;
	}

	/** Returns this aircraft's flight plan (unmodifiable) */
	public List<Waypoint> getFlightPlan()
	{
		return flightPlan;
	}

	/** Returns the last waypoint hit by this aircraft */
	public int getLastWaypoint()
	{
		return lastWaypoint;
	}

	/** Returns the total number of waypoints hit by this aircraft */
	public int getWaypointsHit()
	{
		return waypointsHit;
	}

	@Override
	public void refresh(float dt)
	{
		super.refresh(dt);

		// Test intersection with all remaining waypoints
		for (int i = lastWaypoint + 1; i < flightPlan.size(); i++)
		{
			Vector2D waypointPosition = flightPlan.get(i).getPosition();

			if (position.distanceTo(waypointPosition) <= getSize())
			{
				// Hit it!
				lastWaypoint = i;
				waypointsHit++;
				break;
			}
		}
	}
}
