package seprhou.logic;

public final class Runway {
	private final Vector2D start, end;

	public Runway(Vector2D start, Vector2D end) {
		this.start = start;
		this.end = end;
	}

	public Vector2D getStart() {
		return start;
	}

	public Vector2D getEnd() {
		return end;
	}
}
