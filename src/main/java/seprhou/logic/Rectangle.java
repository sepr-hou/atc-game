package seprhou.logic;

/** An immutable, mathematical 2D vector class */
public class Rectangle
{
	// Class invariant
	//  p2.x >= p1.x && p2.y >= p1.y
	private final Vector2D p1, p2;

	/**
	 * Constructs a new rectangle with the given width and height
	 *
	 * @param width width of rectangle
	 * @param height height of rectangle
	 */
	public Rectangle(float width, float height)
	{
		if (width < 0)
			throw new IllegalArgumentException("width must be at least 0");
		if (height < 0)
			throw new IllegalArgumentException("height must be at least 0");

		this.p1 = Vector2D.ZERO;
		this.p2 = new Vector2D(width, height);
	}

	/**
	 * Constructs a new rectangle with opposite corners point 1 and 2.
	 * 
	 * @param point1 the first vertex of the rectangle
	 * @param point2 the opposite vertex to point 1
	 */
	public Rectangle(Vector2D point1, Vector2D point2)
	{
		if (point1 == null)
			throw new IllegalArgumentException("point1 cannot be null");
		if (point2 == null)
			throw new IllegalArgumentException("point2 cannot be null");

		// Apply class invariant
		if (point2.getX() >= point1.getX() && point2.getY() >= point1.getY())
		{
			// Already ok
			this.p1 = point1;
			this.p2 = point2;
		}
		else if (point2.getX() >= point1.getX() && point2.getY() >= point1.getY())
		{
			// Reversed points
			this.p1 = point2;
			this.p2 = point1;
		}
		else
		{
			// New vectors
			this.p1 = new Vector2D(Math.min(point1.getX(), point2.getX()), Math.min(point1.getY(), point2.getY()));
			this.p2 = new Vector2D(Math.max(point1.getX(), point2.getX()), Math.max(point1.getY(), point2.getY()));
		}
	}

	/** Returns the point in the rectangle with the lowest X and Y coordinates */
	public Vector2D getPoint1()
	{
		return p1;
	}

	/** Returns the point in the rectangle with the highest X and Y coordinates */
	public Vector2D getPoint2()
	{
		return p2;
	}

	/** Returns the height of the rectangle */
	public float getHeight()
	{
		return p2.getY() - p1.getY();
	}

	/** Returns the width of the rectangle */
	public float getWidth()
	{
		return p2.getX() - p1.getX();
	}

	/**
	 * Returns whether or not a point is enclosed within the rectangle
	 *
	 * @param point point to test
	 * @return true if the point lies within this rectangle
	 */
	public boolean contains(Vector2D point)
	{
		float maxY = p2.getY();
		float minY = p1.getY();
		float maxX = p2.getX();
		float minX = p1.getX();

		return  (minY <= point.getY() && point.getY() < maxY) &&
				(minX <= point.getX() && point.getX() < maxX);
	}

	/**
	 * Returns true if this rectangle intersects (overlaps) with another
	 *
	 * @param other other rectangle to compare
	 * @return true if the rectangles intersect
	 */
	public boolean intersects(Rectangle other)
	{
		// Basic bounding box intersection
		return (other.p1.getX() < p2.getX() &&
				other.p2.getX() > p1.getX() &&
				other.p1.getY() < p2.getY() &&
				other.p2.getY() > p1.getY());
	}

	/**
	 * Returns true if this rectangle intersects (overlaps) with a circle
	 *
	 * @param center center of the circle
	 * @param radius radius of the circle
	 * @return true if they intersect
	 */
	public boolean intersects(Vector2D center, float radius)
	{
		/*
		 * either the center is in the rectangle or an edge of the rectangle intersects the circle
		 * if the distance between a corner and the center of the circle is less than the radius then intersection
		 * if N E S or W point of circle in rectangle then intersection
		 */

		float maxY = p2.getY();
		float minY = p1.getY();
		float maxX = p2.getX();
		float minX = p1.getX();

		Vector2D vertex1 = new Vector2D(minX, minY);
		Vector2D vertex2 = new Vector2D(minX, maxY);
		Vector2D vertex3 = new Vector2D(maxX, minY);
		Vector2D vertex4 = new Vector2D(maxX, maxY);

		float distance1 = vertex1.distanceTo(center);
		float distance2 = vertex2.distanceTo(center);
		float distance3 = vertex3.distanceTo(center);
		float distance4 = vertex4.distanceTo(center);

		float maxYcirc = center.getY() + radius;
		float maxXcirc = center.getX() + radius;
		float minYcirc = center.getY() - radius;
		float minXcirc = center.getX() - radius;
		Vector2D northpoint = new Vector2D(center.getX(), maxYcirc);
		Vector2D eastpoint = new Vector2D(maxXcirc, center.getY());
		Vector2D southpoint = new Vector2D(center.getX(), minYcirc);
		Vector2D westpoint = new Vector2D(minXcirc, center.getY());

		if (this.contains(center))
			return true;
		else if ((distance1 < radius)||(distance2 < radius)||(distance3 < radius)||(distance4 < radius))
			return true;
		else if (this.contains(northpoint) || this.contains(eastpoint) || this.contains(southpoint) || this.contains(westpoint))
			return true;
		else
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
	public final boolean equals(Rectangle other)
	{
		return (p1.equals(other.p1) && p2.equals(other.p2));
	}

	@Override
	public final boolean equals(Object other)
	{
		if (this == other)
			return true;

		if (other instanceof Rectangle)
			return equals((Rectangle) other);
        return false;
	}

	@Override
	public final int hashCode()
	{
		int result = p1.hashCode();
		result = 31 * result + p2.hashCode();
		return result;
	}

	@Override
	public String toString()
	{
		return "Rectangle: ((" + p1.getX() + ", " + p1.getY() + ") -> (" + p2.getX() + ", " + p2.getY() + ")) \n" +
				"Height = " + getHeight() + "\n" +
				"Width =" + getWidth() ;
	}
}
