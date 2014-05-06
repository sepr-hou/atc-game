package seprhou.logic;

import java.util.List;

/**
 * An aircraft in the airspace
 * 
 */
public abstract class Aircraft extends AirspaceObject
{
	private static final int DECAY_RATE = 10;

	private final Airspace airspace;
	private final FlightPlan flightPlan;
	private final String name;
	private final float weight;
	private final int crew;
	private AircraftColour colour;

	private float tickCount = 0;

	private boolean violated = false;
	private boolean finished = false;
	private boolean active;

	private int score;
	private int gracePeriod = 30;

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
	protected Aircraft(String name, float weight, int crew, AircraftColour colour, FlightPlan flightPlan, int score, Airspace airspace)
	{
		if (flightPlan == null)
			throw new IllegalArgumentException("flightPlan cannot be null");

		if (name == null)
			name = "";

		// Setup initial general attributes
		this.name = name;
		this.weight = weight;
		this.crew = crew;
		this.colour = colour;
		this.flightPlan = flightPlan;
		this.setScore(score);
		this.airspace = airspace;

		// Setup initial flightplan related attributes
		this.position = flightPlan.getWaypoints().get(0);
		this.velocity = flightPlan.getInitialVelocity();

		if (this.flightPlan.isStartOnRunway()) {
			this.altitude = 0;
			this.targetAltitude = LogicConstants.INITIAL_ALTITUDES.get(Utils
					.getRandom().nextInt(LogicConstants.INITIAL_ALTITUDES.size()));
			Vector2D direction = flightPlan.getWaypoints().get(1)
					.sub(flightPlan.getWaypoints().get(0)).normalize();
			this.velocity = direction;
			this.targetVelocity = direction.multiply(LogicConstants.INITIAL_SPEEDS
					.get(Utils.getRandom().nextInt(
							LogicConstants.INITIAL_SPEEDS.size())));
			this.active = false;
		} else {
			this.altitude = flightPlan.getInitialAltitude();
			this.targetAltitude = this.altitude;
			this.targetVelocity = this.velocity;
			this.active = true;
		}
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
	 * @see Vector2D#getBearing()
	 */
	public float getBearing()
	{
		return this.velocity.getBearing();
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

	/** Sets this aircraft's colour */
	public void setColour(AircraftColour colour)
	{
		this.colour = colour;
	}

	/** Returns this aircraft's colour */
	public AircraftColour getColour()
	{
		return colour;
	}

	/** Returns this aircraft's flight plan (unmodifiable) */
	@Override
	public FlightPlan getFlightPlan()
	{
		return flightPlan;
	}

	/** Returns the last waypoint hit by this aircraft */
	public int getLastWaypoint()
	{
		return lastWaypoint;
	}

	@Override
	public boolean isFinished() {
		return this.finished;
	}

	/** Returns the total number of waypoints hit by this aircraft */
	public int getWaypointsHit()
	{
		return waypointsHit;
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

	/**
	 * Scoring
	 * 
	 * Each plane has a starting score, grace period and score decay rate.
	 * Each second decayScore method is called, but while gracePeriod has not pass,
	 * It keeps decreasing it, when it reaches zero, the method will start to decay score
	 * by decayRate amount every second
	 * 
	 * If another plane breaches exclusion zone of a plane, its score will be decreased by
	 * twice the decay rate and gracePeriod will be ignored.
	 */
	public void decayScore()
	{
		if (this.violated) {
			if (LogicConstants.DEBUG)
				System.out.println("Score violated Deacying:" + this.getScore());
			// This is executed when the exclusion zone of the plane is
			// violated, regardless of gracePeriod
			this.setScore(this.getScore() - DECAY_RATE * 2);
		} else {
			if (this.gracePeriod > 0) {
				if (LogicConstants.DEBUG)
					System.out.println("Grace Remaining:" + this.gracePeriod);
				this.gracePeriod--;
			} else {
				if (LogicConstants.DEBUG)
					System.out.println("Score Decaying:" + this.getScore());
				this.setScore(this.getScore() - DECAY_RATE);
			}
		}
	}

	@Override
	public void setViolated(boolean value){
		this.violated = value;
	}

	@Override
	public boolean isViolated() {
		return this.violated;
	}

	@Override
	public void refresh(float dt)
	{
		super.refresh(dt);

		// Used for precisely measuring the passed time
		// to decrease score only every second
		this.tickCount += dt;
		if( this.tickCount > 1 ){
			this.decayScore();
			this.tickCount = 0;
		}
		
		// Reset violated to false, for checking at the next tick.
		this.violated = false;

		List<Vector2D> waypoints = this.flightPlan.getWaypoints();
		// If the plane start on runway, return control to player after the
		// plane takes off from airport.
		if (flightPlan.isStartOnRunway() && this.lastWaypoint == 1)
			this.active = true;

		// Test intersection with all remaining waypoints
		if (this.lastWaypoint + 1 >= waypoints.size())
		{
			this.finished = true;
		}
		else
		{
			Vector2D waypointPosition = waypoints.get(this.lastWaypoint + 1);

			if (this.position.distanceTo(waypointPosition) <= this.getSize()) {
				// Landing on runway
				if (this.lastWaypoint + 3 == waypoints.size()
						&& this.flightPlan.isLanding())
				{
					double angle = (waypoints.get(waypoints.size() - 1).sub(waypoints.get(waypoints.size() - 2))).getBearing();
					// Checks that the plane is approaching airport
					// at correct angle, altitude and speed
					if (this.getBearing() > angle - 25
							&& this.getBearing() < angle + 25
							&& Math.abs(this.getAltitude()
									- this.getMinAltitude()) < 1
							&& Math.abs(this.getVelocity().getLength()
									- this.getMinSpeed()) < 1
							&& this.airspace.getLandedObjects()
									+ this.airspace.getLandingPlanes() < 10)
					{
						// Increment the counter of landing planes.
						this.airspace.setLandingPlanes(this.airspace.getLandingPlanes() + 1);
						// Remove control of the plane from the player.
						this.active = false;
						// Land the plane automatically.
						this.setTargetVelocityNoClamping(waypoints
								.get(waypoints.size() - 1).sub(this.position)
								.changeLength(30f));
						this.setTargetAltitudeNoClamping(0);
					}
					else
					{
						//If the next waypoint of the plane is start of runway,
						//but the plane is approaching the airport at wrong parameters
						//return without incrementing pointers.
						return;
					}
				}

				// Increment the pointer to the waypoints list
				this.lastWaypoint++;
				this.waypointsHit++;
				// Increase the grace period of the plane for hitting a waypoint
				this.gracePeriod += 5;
			}
		}
	}
}
