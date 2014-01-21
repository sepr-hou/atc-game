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

	/**
	 * Creates a new vector from the given polar coordinates
	 *
	 * @param length length of the vector (negative values reverse the direction)
	 * @param angle angle of the vector (radians)
	 * @return the new vector
	 */
	public static Vector2D fromPolar(float length, float angle)
	{
		float newX = (float) (length * Math.cos(angle));
		float newY = (float) (length * Math.sin(angle));

		return new Vector2D(newX, newY);
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
		return this.getX() * this.getX() + this.getY() * this.getY();
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
		return (float) Math.atan2(this.getY(), this.getX());
	}

	/**
	 * Returns the distance between this vector and another vector
	 *
	 * @param other other vector
	 * @return the distance between them (always >= 0)
	 */
	public float distanceTo(Vector2D other)
	{
		float diffx = Math.max((this.getX() - other.x), (other.getX() - this.x));
		float diffy = Math.max((this.getY() - other.y), (other.getY() - this.y));
		float sumsqr = ((diffx * diffx) + (diffy * diffy));
		return  (float) Math.sqrt(sumsqr);
	}

	/**
	 * Adds this vector to another
	 *
	 * @param other other vector to add to
	 * @return a new vector containing the result
	 */
	public Vector2D add(Vector2D other)
	{
		return new Vector2D((this.getX() + other.getX()),(this.getY() + other.getY()));
	}

	/**
	 * Subtracts another vector from this vector
	 *
	 * @param other other vector to subtract
	 * @return a new vector containing the result
	 */
	public Vector2D sub(Vector2D other)
	{
		return new Vector2D((this.getX() - other.getX()),(this.getY() - other.getY()));
	}

	/**
	 * Multiplies this vector by a scalar
	 *
	 * @param scalar value to multiply by
	 * @return a new vector containing the result
	 */
	public Vector2D multiply(float scalar)
	{
		return new Vector2D((this.getX() * scalar),(this.getY() * scalar));
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
		float cosang = (float) Math.cos(angle);
		float sinang = (float) Math.sin(angle);
		return new Vector2D((this.getX() * cosang) - (this.getY() * sinang),(this.getX() * sinang) + (this.getY() * cosang));
	}

	/**
	 * Returns a normalized version of this vector
	 *
	 * @return this vector with length 1
	 */
	public Vector2D normalize()
	{
		return changeLength(1);
	}

	/**
	 * Creates a vector with the given length but the same angle as this vector
	 *
	 * @param newLength length of the new vector
	 * @return a new vector containing the result
	 */
	public Vector2D changeLength(float newLength)
	{
		float myLength = getLength();

		if (myLength == 0)
			return XAXIS.multiply(newLength);

		return multiply(newLength / myLength);
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
	public final boolean equals(Vector2D other)
	{
		if (other == null)
			return false;

		return Float.floatToIntBits(this.getX()) == Float.floatToIntBits(other.getX()) &&
				Float.floatToIntBits(this.getY()) == Float.floatToIntBits(other.getY());
	}

	@Override
	public final boolean equals(Object other)
	{
		if (other == null)
			return false;
		if (this == other)
			return true;

		if (other instanceof Vector2D)
			return equals((Vector2D) other);

		return false;
	}

	@Override
	public final int hashCode()
	{
		int result = Float.floatToIntBits(x);
		result = 31 * result + Float.floatToIntBits(y);
		return result;
	}

	@Override
	public String toString()
	{
		float angle = getAngle();
		float angleDegrees = angle * (float) (180.0 / Math.PI);

		return "Vector2D: (" + getX() + ", " + getY() + ") \n" +
				" r = " + getLength() + ", θ = " + angle + ", " + angleDegrees + "°" ;
	}
}
