package seprhou.logic;

import java.util.List;

/**
 * Class which stores the flight plan of an aircraft and its initial trajectory
 */
public class FlightPlan {
	private final List<Vector2D> waypoints;
	private final float initialSpeed, initialAltitude;
	private final boolean landing;
	private final boolean startOnRunway;

	/**
	 * Creates a new flight plan
	 * 
	 * @param waypoints list of waypoint positions
	 * @param initialSpeed initial speed of the flight
	 * @param initialAltitude initial altitude of the flight
	 */
	public FlightPlan(List<Vector2D> waypoints, float initialSpeed, float initialAltitude, boolean landing, boolean startOnRunway) {
		if (waypoints == null) {
			throw new IllegalArgumentException("waypoints cannot be null");
		}
		if (waypoints.size() < 2) {
			throw new IllegalArgumentException("waypoints must have at least 2 items");
		}

		this.waypoints = waypoints;
		this.initialSpeed = initialSpeed;
		this.initialAltitude = initialAltitude;
		this.landing = landing;
		this.startOnRunway = startOnRunway;
	}

	/** Returns the list of waypoints */
	public List<Vector2D> getWaypoints() {
		return this.waypoints;
	}

	/** Returns the initial speed */
	public float getInitialSpeed() {
		return this.initialSpeed;
	}

	/** Returns the flight's initial velocity calculated from its initial speed */
	public Vector2D getInitialVelocity() {
		// Get direction vector
		Vector2D directionVector = this.waypoints.get(1).sub(this.waypoints.get(0));

		// Scale to match initial speed
		return directionVector.changeLength(this.initialSpeed);
	}

	/** Returns the initial altitude */
	public float getInitialAltitude() {
		return this.initialAltitude;
	}

	public boolean isLanding() {
		return this.landing;
	}

	public boolean isStartOnRunway() {
		return startOnRunway;
	}
}
