package seprhou.logic;

import java.util.List;

/**
 * An aircraft in the airspace
 * 
 */
public abstract class Aircraft extends AirspaceObject {
	private final String name;
	private final float weight;
	private final int crew;
	private float tickCount = 0;

	private boolean violated = false;
	private boolean finished = false;

	private int score;
	private int gracePeriod = 30;
	private int decayRate = 10;

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
	protected Aircraft(String name, float weight, int crew, FlightPlan flightPlan, int score) {
		if (flightPlan == null) {
			throw new IllegalArgumentException("flightPlan cannot be null");
		}

		if (name == null) {
			name = "";
		}

		// Setup my attributes
		this.name = name;
		this.weight = weight;
		this.crew = crew;
		this.flightPlan = flightPlan;
		this.score = score;

		// Setup initial object attributes
		this.position = flightPlan.getWaypoints().get(0);
		this.velocity = flightPlan.getInitialVelocity();
		this.altitude = flightPlan.getInitialAltitude();

		this.targetVelocity = this.velocity;
		this.targetAltitude = this.altitude;
	}

	/** Returns this aircraft's name */
	public String getName() {
		return this.name;
	}

	/**
	 * Returns this aircraft's bearing as calculated from its velocity
	 * 
	 * <p>
	 * This means that this method returns an angle from 0 to 360 where 0
	 * degrees points upwards (positive y direction).
	 * 
	 * @return the bearing of this aircraft
	 */
	public float getBearing() {
		float angle = this.velocity.getAngle();
		angle = angle * 180;
		angle /= Math.PI;
		angle = -angle;
		angle += 90;
		if (angle < 0) {
			angle += 360;
		}
		return angle;
	}

	/** Returns this aircraft's weight */
	public float getWeight() {
		return this.weight;
	}

	/** Returns this aircraft's crew */
	public int getCrew() {
		return this.crew;
	}

	/** Returns this aircraft's flight plan (unmodifiable) */
	public FlightPlan getFlightPlan() {
		return this.flightPlan;
	}

	/** Returns the last waypoint hit by this aircraft */
	public int getLastWaypoint() {
		return this.lastWaypoint;
	}

	@Override
	public boolean getFinished() {
		return this.finished;
	}

	/** Returns the total number of waypoints hit by this aircraft */
	public int getWaypointsHit() {
		return this.waypointsHit;
	}

	public float getTickCount() {
		return this.tickCount;
	}

	public void setTickCount(float count) {
		this.tickCount = count;
	}

	/**
	 * Scoring
	 * 
	 * Each plane has a starting score, grace period and score decay rate. Each
	 * second decayScore method is called, but while gracePeriod has not pass,
	 * It keeps decreasing it, when it reaches zero, the method will start to
	 * decay score by decayRate amount every second
	 * 
	 * If another plane breaches exclusion zone of a plane, its score will be
	 * decreased by twice the decay rate and gracePeriod will be ignored.
	 */
	public void decayScore() {
		if (this.violated) {
			System.out.println("Score violated Deacying:" + this.score);
			// This is executed when the exclusion zone of the plane is
			// violated, regardless of gracePeriod
			this.score -= this.decayRate * 2;
		} else {
			if (this.gracePeriod > 0) {
				System.out.println("Grace Remaining:" + this.gracePeriod);
				this.gracePeriod--;
			} else {
				System.out.println("Score Decaying:" + this.score);
				this.score = this.score - this.decayRate;
			}
		}
	}

	@Override
	public void setViolated(boolean value) {
		this.violated = value;
	}

	@Override
	public boolean getViolated() {
		return this.violated;
	}

	@Override
	public void refresh(float dt) {

		super.refresh(dt);

		this.tickCount += dt;
		if (this.tickCount > 1) {
			this.decayScore();
			this.setTickCount(0);
		}

		// Reset violated to false, for checking at the next tick.
		this.violated = false;
		// Test intersection with all remaining waypoints
		List<Vector2D> waypoints = this.flightPlan.getWaypoints();

		if (this.lastWaypoint + 1 >= waypoints.size()) {
			this.finished = true;
		} else {

			Vector2D waypointPosition = waypoints.get(this.lastWaypoint + 1);

			if (this.position.distanceTo(waypointPosition) <= this.getSize()) {
				// Hit it!
				this.lastWaypoint++;
				this.waypointsHit++;
				this.gracePeriod += 5;
			}
		}

	}

}
