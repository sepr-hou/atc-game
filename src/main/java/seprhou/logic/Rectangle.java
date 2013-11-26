package seprhou.logic;

/** An immutable, mathematical 2D vector class */
public class Rectangle
{
	private final Vector2D p1, p2;

	/**
	 * Constructs a new rectangle with opposite corners point 1 and 2.
	 * 
	 * @param point1 the first vertex of the rectangle
	 * @param point2 the opposite vertex to point 1
	 */
	public Rectangle(Vector2D point1, Vector2D point2)
	{
		this.p1 = point1;
		this.p2 = point2;
	}

	/** Returns the height of the rectangle */
	public float getHeight()
	{
		float height;
		height = p1.getY() - p2.getY();
		if (height >= 0)
			return height;
		else
			return -height;
	}

	/** Returns the width of the rectangle */
	public float getWidth()
	{
		float width;
		width = p1.getX() - p2.getX();
		if (width >= 0)
			return width;
		else
			return -width;
	}

	/** Returns whether or not a point is enclosed within the rectangle */
	public boolean contains(Vector2D point)
	{
		float maxY = (float) Math.max(p1.getY(), p2.getY());
		float minY = (float) Math.min(p1.getY(), p2.getY());
		float maxX = (float) Math.max(p1.getX(), p2.getX());
		float minX = (float) Math.min(p1.getX(), p2.getX());
		if (maxY - point.getY() >= minY && maxX - point.getX() >= minX)
			return true;
		else
			return false;
	}

	/** Returns whether or not one rectangle intersects another */
	public boolean intersects(Rectangle other)
	{
		// TODO Implement this
		return false;
	}

	/** Returns whether or not the rectangle intersects a circle with center at the given point and of the radius given. */
	public boolean intersects(Vector2D center, float radius)
	{
		// TODO Implement this
		return false;
	}

	/**
	 * Returns true if this rectangle is exactly equal to another
	 *
	 * <p>
	 * You probably DO NOT want to be calling this method.
	 *
	 * <p>
	 * Due to floating point rounding errors, this method is slightly dangerous
	 * and can return values you might not expect.
	 *
	 * @param other other rectangle to compare
	 * @return true if the 2 points are equal in both rectangles
	 */
	public boolean equals(Rectangle other)
	{
		// TODO Implement this
		return false;
	}

	@Override
	public boolean equals(Object other)
	{
		// TODO Implement this
		return false;
	}

	@Override
	public int hashCode()
	{
		// TODO Implement this
		return 0;
	}

	@Override
	public String toString()
	{
		// TODO Implement this
		return null;
	}
}
