package seprhou.logic;

public class CollisionWarning {
	private Aircraft aircraft1, aircraft2;
	
	public CollisionWarning(Aircraft aircraft1, Arcraft aircraft2)
	{
	this.aircraft1 = aircraft1;
	this.aircraft2 = aircraft2;
	}

	public Aircraft getAircraft1() {
		return aircraft1;
	}

	public void setAircraft1(Aircraft aircraft1) {
		this.aircraft1 = aircraft1;
	}

	public Aircraft getAircraft2() {
		return aircraft2;
	}

	public void setAircraft2(Aircraft aircraft2) {
		this.aircraft2 = aircraft2;
	}
	public boolean hasCollided(){
		
	}
	public float getHorizDistance(){
		
	}
	public float getVertDistance(){
		
	}
}
