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
		
	}
	
	public float getWidth()
	{
		
	}
	
	public boolean contains(Vector2D point)
	{
		
	}
	
	public boolean intersects(Rectangle other)
	{
	}
	
	public boolean intersects(Vector2D centre, float radius)
	{
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
    }

    @Override
    public boolean equals(Object other)
    {
    }

    @Override
    public int hashCode()
    {
    }

    @Override
    public String toString()
    {
    }
}
