package seprhou.logic;

/** An immutable, mathematical 2D vector class */
public class Vector2D
{
	private final float x, y;

	/** The null vector with 0 length and no direction */
	public final static Vector2D ZERO = new Vector2D(0, 0);

	/** A unit vector pointing down the X axis */
	public final static Vector2D XAXIS = new Vector2D(1, 0);

	/**
	 * Constructs a new vector with the given X and Y values
	 *
	 * @param x the X value of the vector
	 * @param y the Y value of the vector
	 */
	public Vector2D(float x, float y)
	{
		this.x = x;
		this.y = y;
	}

	/** Returns the X value of this vector */
	public float getX()
	{
		return x;
	}

	/** Returns the Y value of this vector */
	public float getY()
	{
		return y;
	}

	/** Returns the length (euclidean norm) of this vector */
	public float getLength()
	{
		return (float) Math.sqrt(this.getX() * this.getX() + this.getY() * this.getY());
	}

	/**
	 * Returns the length of this vector squared
	 *
	 * <p>
	 * This method exists because it avoids the square root which makes {@link #getLength()} slow.
	 * If you just need to compare lengths, you can use this method instead.
	 *
	 * @return the length of this vector, squared
	 * @see #getLength()
	 */
	public float getLengthSquared()
	{
		return (float) (this.getX() * this.getX() + this.getY() * this.getY());
	}

	/**
	 * Returns the angle between this vector to the X axis
	 *
	 * <p>This method always returns angles between -pi and pi.
	 * <p>Positive angles are assigned to vectors with positive Y values.
	 * This means that if the Y axis points down, increasing the angle moves clockwise.
	 * If the Y axis points up, increasing the angle moves anticlockwise.
	 *
	 * @return the angle of this vector is radians between -pi and pi
	 * @see java.lang.Math#atan2(double, double)
	 */
	public float getAngle()
	{
		/* dot product between vector and axis is equal to vector.x */
		/* therefore cos(angle) = x / |vector|*/
		float angle = Math.acos(this.getX() / this.getLength());
		return angle;
	}

	/**
	 * Returns the distance between this vector and another vector
	 *
	 * @param other other vector
	 * @return the distance between them (always >= 0)
	 */
	public float distanceTo(Vector2D other)
	{
		float diffx = Math.max((this.getX() - other.getX), (other.getX() - this.getX));
		float diffy = Math.max((this.getY() - other.getY), (other.getY() - this.getY));
		float sumsqr = ((diffx * diffx) + (diffy * diffy));
		float result = Math.sqrt(sumsqr);
		return result;
	}

	/**
	 * Adds this vector to another
	 *
	 * @param other other vector to add to
	 * @return a new vector containing the result
	 */
	public Vector2D add(Vector2D other)
	{
		Vector2D result = new Vector2D((this.getX() + other.getX()),(this.getY() + other.getY()));
		return result;
	}

	/**
	 * Subtracts another vector from this vector
	 *
	 * @param other other vector to subtract
	 * @return a new vector containing the result
	 */
	public Vector2D sub(Vector2D other)
	{
		Vector2D result = new Vector2D((this.getX() - other.getX()),(this.getY() - other.getY()));;
		return result;
	}

	/**
	 * Multiplies this vector by a scalar
	 *
	 * @param scalar value to multiply by
	 * @return a new vector containing the result
	 */
	public Vector2D multiply(float scalar)
	{
		Vector2D result = new Vector2D((this.getX() * scalar),(this.getY() * scalar));
		return result;
	}

	/**
	 * Rotates this vector around the origin by the given angle
	 *
	 * <p>See {@link #getAngle()} for information about which way this rotates.
	 *
	 * @param angle angle to rotate by in radians
	 * @return a new vector containing the result
	 */
	public Vector2D rotate(float angle)
	{
		float cosang = Math.cos(angle);
		float sinang = Math.sin(angle);
		Vector2D result = new Vector2D((this.getX() * cosang) - (this.getY() * sinang),(this.getX() * sinang) + (this.getY() * cosang));
		return result;
	}

	/**
	 * Returns true if this vector is exactly equal to another
	 *
	 * <p>
	 * You probably DO NOT want to be calling this method.
	 *
	 * <p>
	 * Due to floating point rounding errors, this method is slightly dangerous
	 * and can return values you might not expect.
	 *
	 * @param other other vector to compare
	 * @return true if x and y are equal in both vectors
	 */
	public boolean equals(Vector2D other)
	{
		if (other.getX() == this.getX() && other.getY() == this.getY())
				return true;
		else
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
