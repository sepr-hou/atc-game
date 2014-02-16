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
	private final Airspace airspace;

	private boolean violated = false;
	private boolean finished = false;
	private boolean startOnRunway = false;
	private boolean active;

	private int score;
	private int gracePeriod = 30;
	private int decayRate = 10;

	private FlightPlan flightPlan;

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
	protected Aircraft(String name, float weight, int crew,
			FlightPlan flightPlan, int score, Airspace airspace) {
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
		this.airspace = airspace;

		// Setup initial object attributes
		this.position = flightPlan.getWaypoints().get(0);
		this.velocity = flightPlan.getInitialVelocity();

		if (this.flightPlan.isStartOnRunway()) {
			this.altitude = 0;
			this.targetAltitude = Constants.INITIAL_ALTITUDES.get(Utils
					.getRandom().nextInt(Constants.INITIAL_ALTITUDES.size()));
			Vector2D direction = flightPlan.getWaypoints().get(1)
					.sub(flightPlan.getWaypoints().get(0)).normalize();
			this.velocity = direction;
			this.targetVelocity = direction.multiply(Constants.INITIAL_SPEEDS
					.get(Utils.getRandom().nextInt(
							Constants.INITIAL_SPEEDS.size())));
			this.active = false;
			this.startOnRunway = true;
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

	/**
	 * Returns this vector's angle
	 * 
	 * <p>
	 * This means that this method returns an angle from 0 to 360 where 0
	 * degrees points upwards (positive y direction).
	 * 
	 * @return the angle of this vector
	 */
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

	/** Assigns this aircraft a new flight plan. **/
	public void setFlightPlan(FlightPlan flightPlan) {
		this.flightPlan = flightPlan;
	}

	/** Returns the last waypoint hit by this aircraft */
	public int getLastWaypoint() {
		return this.lastWaypoint;
	}

	// Prepares the plane for taking off.
	// Resets all of its values, and assigns a new flightplan
	public void resetRunwayPlane() {
		this.lastWaypoint = 0;
		this.waypointsHit = 0;
		this.score = 1000;
		this.gracePeriod = 30;
		this.finished = false;
		this.startOnRunway = true;
		this.active = false;
		this.tickCount = 0;
		this.altitude = 0;
		this.position = flightPlan.getWaypoints().get(0);
		this.targetAltitude = Constants.INITIAL_ALTITUDES.get(Utils.getRandom()
				.nextInt(Constants.INITIAL_ALTITUDES.size()));
		Vector2D direction = flightPlan.getWaypoints().get(1)
				.sub(flightPlan.getWaypoints().get(0)).normalize();
		this.velocity = direction;
		this.targetVelocity = direction
				.multiply(Constants.INITIAL_SPEEDS.get(Utils.getRandom()
						.nextInt(Constants.INITIAL_SPEEDS.size())));
		this.active = false;
		this.startOnRunway = true;
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

	// Convenience function to change the plane bearing
	// Without having to manually modify velocity
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
		if (this.startOnRunway && this.lastWaypoint == 1) {
			this.active = true;
		}
		if (this.lastWaypoint + 1 >= waypoints.size()) {
			this.finished = true;
		} else {

			Vector2D waypointPosition = waypoints.get(this.lastWaypoint + 1);

			if (this.position.distanceTo(waypointPosition) <= this.getSize()) {
				// Landing on runway
				if (this.lastWaypoint + 3 == waypoints.size()
						&& this.flightPlan.isLanding()) {
					double angle = this.calculateAngle(waypoints.get(
							waypoints.size() - 1).sub(
							waypoints.get(waypoints.size() - 2)));
					// Checks that the plane is approaching airport
					// at correct angle, altitude and speed
					if (this.getBearing() > angle - 25
							&& this.getBearing() < angle + 25
							&& Math.abs(this.getAltitude()
									- this.getMinAltitude()) < 1
							&& Math.abs(this.getVelocity().getLength()
									- this.getMinSpeed()) < 1
							&& this.airspace.getLandedObjects().size()
									+ this.airspace.getLandingPlanes() < 10) {
						// Increment the counter of landing planes.
						this.airspace.setLandingPlanes(this.airspace.getLandingPlanes() + 1);
						// Remove control of the plane from the player.
						this.active = false;
						// Land the plane automatically.
						this.setTargetVelocityNoClamping(waypoints
								.get(waypoints.size() - 1).sub(this.position)
								.changeLength(30f));
						this.setTargetAltitudeNoClamping(0);
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
