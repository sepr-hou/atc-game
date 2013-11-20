package seprhou.logic;

public class Waypoint {
	private Vector2D position;
	private float speed, altitude;
	
	public Waypoint(Vector2D position, float speed, float altitude)
	{
	this.position = position;
	this.speed = speed;
	this.altitude = altitude;
	}

	public Vector2D getPosition() {
		return position;
	}

	public void setPosition(Vector2D position) {
		this.position = position;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getAltitude() {
		return altitude;
	}

	public void setAltitude(float altitude) {
		this.altitude = altitude;
	}
	
}
