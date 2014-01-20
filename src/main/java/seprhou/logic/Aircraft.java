package seprhou.logic;

import java.util.List;

/**
 * An aircraft in the airspace
 */
public abstract class Aircraft extends AirspaceObject
{
	private final String name;
	private final float weight;
	private final int crew;

	private final FlightPlan flightPlan;

	private int lastWaypoint;
	private int waypointsHit;

	/**
	 * Constructs a new aircraft
	 *
	 * @param name aircraft name (descriptive only)
	 * @param weight aircraft weight
	 * @param crew number of crew
	 * @param flightPlan aircraft flight plan
	 */
	protected Aircraft(String name, float weight, int crew, FlightPlan flightPlan)
	{
		if (flightPlan == null)
			throw new IllegalArgumentException("flightPlan cannot be null");

		if (name == null)
			name = "";

		// Setup my attributes
		this.name = name;
		this.weight = weight;
		this.crew = crew;
		this.flightPlan = flightPlan;

		// Setup initial object attributes
		this.position = flightPlan.getWaypoints().get(0);
		this.velocity = flightPlan.getInitialVelocity();
		this.altitude = flightPlan.getInitialAltitude();

		this.targetVelocity = this.velocity;
		this.targetAltitude = this.altitude;
	}

	/** Returns this aircraft's name */
	public String getName()
	{
		return name;
	}

	/**
	 * Returns this aircraft's bearing as calculated from its velocity
	 *
	 * <p>
	 * This means that this method returns an angle from 0 to 360 where 0 degrees
	 * points upwards (positive y direction).
	 *
	 * @return the bearing of this aircraft
	 */
	public float getBearing()
	{
		float angle = this.velocity.getAngle();
		angle = angle * 180;
		angle /= Math.PI;
		angle = -angle;
		angle += 90;
		if (angle < 0){
			angle += 360;
		}
		return angle;
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
	public FlightPlan getFlightPlan()
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
		List<Vector2D> waypoints = flightPlan.getWaypoints();

		for (int i = lastWaypoint + 1; i < waypoints.size(); i++)
		{
			Vector2D waypointPosition = waypoints.get(i);

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
