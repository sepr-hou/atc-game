package seprhou.logic;

/** An immutable subclass of FlightPlan */

public class Waypoint
{
	private Vector2D position;
	private float speed, altitude;

	/**
	 * Constructs a new waypoint with the given position, speed and altitude.
	 *
	 * @param position the location of the waypoint in the airspace
	 * @param speed    the speed of the aircraft at this waypoint
	 * @param altitude the altitude of the aircraft at this waypoint
	 */
	public Waypoint(Vector2D position, float speed, float altitude)
	{
		this.position = position;
		this.speed = speed;
		this.altitude = altitude;
	}

	/** returns the position of the waypoint */
	public Vector2D getPosition()
	{
		return position;
	}

	/** changes the position of the waypoint */
	public void setPosition(Vector2D position)
	{
		this.position = position;
	}

	/** returns the speed of the aircraft at the waypoint */
	public float getSpeed()
	{
		return speed;
	}

	/** changes the speed of the aircraft at the waypoint */
	public void setSpeed(float speed)
	{
		this.speed = speed;
	}

	/** returns the altitude of the aircraft at the waypoint */
	public float getAltitude()
	{
		return altitude;
	}

	/** changes the altitude of the aircraft at the waypoint */
	public void setAltitude(float altitude)
	{
		this.altitude = altitude;
	}

}
