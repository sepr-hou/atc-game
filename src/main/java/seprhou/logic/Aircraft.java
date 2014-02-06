package seprhou.logic;

import java.util.List;

import seprhou.gui.Constants;

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
	private boolean active;

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
	protected Aircraft(String name, float weight, int crew, FlightPlan flightPlan, int score, boolean startOnRunway) {
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
		this.setScore(score);

		// Setup initial object attributes
		this.position = flightPlan.getWaypoints().get(0);
		this.velocity = flightPlan.getInitialVelocity();

		if (startOnRunway) {
			this.altitude = 0;
			this.targetAltitude = Constants.INITIAL_ALTITUDES.get(Utils.getRandom().nextInt(Constants.INITIAL_ALTITUDES.size()));
			Vector2D direction = flightPlan.getWaypoints().get(1).sub(flightPlan.getWaypoints().get(0)).normalize();
			this.velocity = direction;
			this.targetVelocity = direction.multiply(Constants.INITIAL_SPEEDS.get(Utils.getRandom().nextInt(Constants.INITIAL_SPEEDS.size())));
			this.active = false;
		} else {
			this.altitude = flightPlan.getInitialAltitude();
			this.targetAltitude = this.altitude;
			this.targetVelocity = this.velocity;
			this.active = true;
		}
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

	public float calculateAngle(Vector2D vector) {
		float angle = vector.getAngle();
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
	public boolean isFinished() {
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

	@Override
	public int getScore() {
		return this.score;
	}

	public void setScore(int score) {
		this.score = score;
	}

	public boolean isActive() {
		return this.active;
	}

	public void setBearing(float bearing) {
		float delta = (float) ((this.getBearing() - bearing) * Math.PI / 180);
		if (delta > 0) {
			this.velocity = this.velocity.rotate(delta);
		} else {
			this.velocity = this.velocity.rotate(-delta);
		}
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
			// System.out.println("Score violated Deacying:" + this.getScore());
			// This is executed when the exclusion zone of the plane is
			// violated, regardless of gracePeriod
			this.setScore(this.getScore() - this.decayRate * 2);
		} else {
			if (this.gracePeriod > 0) {
				// System.out.println("Grace Remaining:" + this.gracePeriod);
				this.gracePeriod--;
			} else {
				// System.out.println("Score Decaying:" + this.getScore());
				this.setScore(this.getScore() - this.decayRate);
			}
		}
	}

	@Override
	public void setViolated(boolean value) {
		this.violated = value;
	}

	@Override
	public boolean isViolated() {
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
				// Landing on runway
				if (this.lastWaypoint + 3 >= waypoints.size() && this.flightPlan.isLanding()) {
					double angle = this.calculateAngle(waypoints.get(waypoints.size() - 1).sub(waypoints.get(waypoints.size() - 2)));


					if (this.getBearing() > angle - 5 && this.getBearing() < angle + 5) {
						this.active = false;
						this.setTargetVelocity(waypoints.get(waypoints.size() - 1).sub(waypoints.get(waypoints.size() - 2)).changeLength(this.getVelocity().getLength()));
					} else {
						return;
					}
				}

				this.lastWaypoint++;
				this.waypointsHit++;
				this.gracePeriod += 5;
			}
		}

	}

}
