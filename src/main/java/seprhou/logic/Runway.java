package seprhou.logic;

public final class Runway {
	private final Vector2D start, end;

	/**
	 * Simple class to represent the runway Only holds its starting and ending
	 * points
	 * */
	public Runway(Vector2D start, Vector2D end) {
		this.start = start;
		this.end = end;
	}

	public Vector2D getStart() {
		return this.start;
	}

	public Vector2D getEnd() {
		return this.end;
	}
}
