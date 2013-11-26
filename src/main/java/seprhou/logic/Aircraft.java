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
		this.name = name;
		this.weight = weight;
		this.crew = crew;
		this.flightPlan = Collections.unmodifiableList(flightPlan);

		// TODO Set initial position + velocity values
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

		// TODO Update waypoints hit
	}
}
