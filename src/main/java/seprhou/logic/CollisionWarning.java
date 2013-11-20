package seprhou.logic;
/**
 * An immutable subclass of Airspace
 *
 */
public class CollisionWarning {
	private Aircraft aircraft1, aircraft2;
	/**
	 * Constructs a warning involving two aircraft
	 * @param aircraft1 the first aircraft
	 * @param aircraft2 the second aircraft
	 */
	public CollisionWarning(Aircraft aircraft1, Arcraft aircraft2)
	{
	this.aircraft1 = aircraft1;
	this.aircraft2 = aircraft2;
	}
	/** returns the first aircraft */
	public Aircraft getAircraft1() {
		return aircraft1;
	}
	/** changes the first aircraft */
	public void setAircraft1(Aircraft aircraft1) {
		this.aircraft1 = aircraft1;
	}
	/** returns the second aircraft */
	public Aircraft getAircraft2() {
		return aircraft2;
	}
	/** changes the second aircraft */
	public void setAircraft2(Aircraft aircraft2) {
		this.aircraft2 = aircraft2;
	}
	/** returns whether or not the aircraft have collided */
	public boolean hasCollided(){
		
	}
	/** returns the horizontal distance between the aircraft */
	public float getHorizDistance(){
		
	}
	/** returns the vertical distance between the aircraft */
	public float getVertDistance(){
		
	}
}
