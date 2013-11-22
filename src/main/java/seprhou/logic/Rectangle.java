package seprhou.logic;

public class Rectangle
{
	private final Vector2D p1, p2;

	public Rectangle(Vector2D point1, Vector2D point2)
	{
		this.p1 = point1;
		this.p2 = point2;
	}

	public float getHeight()
	{
		// TODO Implement this
		return 0;
	}

	public float getWidth()
	{
		// TODO Implement this
		return 0;
	}

	public boolean contains(Vector2D point)
	{
		// TODO Implement this
		return false;
	}

	public boolean intersects(Rectangle other)
	{
		// TODO Implement this
		return false;
	}

	public boolean intersects(Vector2D centre, float radius)
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
