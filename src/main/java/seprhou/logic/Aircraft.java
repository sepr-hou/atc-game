package seprhou.logic;

/**
 * An aircraft within an airspace
 *
 * <p>
 * The current state of an aircraft is contained within its 2d position, 2d velocity and altitude.
 * Each refresh, the aircraft's position is updated according to its velocity.
 * In addition to this, aircraft also have a target velocity and altitude.
 * Each refresh, the aircraft's velocity and altitude are updated to get closer to the targets at a
 * rate specified in the aircraft's type.
 */
public class Aircraft
{
    // Current state of the motion of the aircraft (position + 2d velocity)
    private Vector2D position, velocity;
    private float altitude;

    // Targets
    private Vector2D targetVelocity;
    private float targetAltitude;

    // Flightplan information
    private final FlightPlan flightPlan;
    private int lastWaypoint, waypointsHit;

    // True if aircraft is active
    private boolean active;

    // Type of aircraft
    private final AircraftType type;

    /**
     * Constructs a new aircraft with a fixed flightplan and type
     *
     * @param flightPlan flight plan of this aircraft
     * @param type type of this aircraft
     */
    public Aircraft(FlightPlan flightPlan, AircraftType type)
    {
        this.flightPlan = flightPlan;
        this.type = type;
    }

    /** Returns this aircraft's flight plan */
    public FlightPlan getFlightPlan()
    {
        return flightPlan;
    }

    /** Returns this aircraft's type */
    public AircraftType getAircraftType()
    {
        return type;
    }

    /** Returns this aircraft's current position */
    public Vector2D getPosition()
    {
        return position;
    }

    /** Returns this aircraft's current velocity */
    public Vector2D getVelocity()
    {
        return velocity;
    }

    /** Returns this aircraft's current altitude */
    public float getAltitude()
    {
        return altitude;
    }

    /** Returns this aircraft's target velocity */
    public Vector2D getTargetVelocity()
    {
        return targetVelocity;
    }

    /**
     * Sets a new target velocity for this aircraft
     *
     * <p>The aircraft will rotate and change speed towards its new target velocity
     *
     * @param newVelocity new target velocity
     */
    public void setTargetVelocity(Vector2D newVelocity)
    {
        this.targetVelocity = newVelocity;
    }

    /** Returns this aircraft's target altitude */
    public float getTargetAltitude()
    {
        return targetAltitude;
    }

    /**
     * Sets a new target altitude for this aircraft
     *
     * <p>The aircraft will start climbing / falling towards its target altitude
     *
     * @param newAltitude new target altitude
     */
    public void setTargetAltitude(float newAltitude)
    {
        this.targetAltitude = newAltitude;
    }

    /** Returns the id of the last waypoint hit in this aircraft's flightpath */
    public int getLastWaypointId()
    {
        return lastWaypoint;
    }

    /** Returns the number of waypoints this aircraft has hit so far */
    public int getWaypointsHit()
    {
        return waypointsHit;
    }

    /** Returns true if this aircraft is active (subject to moving + collisions) */
    public boolean isActive()
    {
        return active;
    }

    /**
     * Sets whether this aircraft is active
     *
     * <p>
     * Active aircraft can move and collide with other aircraft.
     * Inactive aircraft do not move and will not cause collisions.
     * This can be used for aircraft which have landed at airports.
     *
     * @param active new active value
     */
    public void setActive(boolean active)
    {
        this.active = active;
    }

    /**
     * Called every game tick to update this aircraft's position and other data
     *
     * @param dt number of seconds elapsed since the last call to this method
     * @see Airspace#refresh(float)
     */
    public void refresh(float dt)
    {
        // TODO Implement refresh
    }
}
