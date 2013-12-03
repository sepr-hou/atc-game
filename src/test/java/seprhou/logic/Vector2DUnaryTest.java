package seprhou.logic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertThat;
import static seprhou.logic.IsCloseToFloat.closeTo;

/**
 * Contains the unary tests for {@link Vector2D}
 *
 * <p>
 * Unary tests are tests which apply a unary operator to the vector to get a value.
 *
 * <p>
 * This is a parameterized test, so lots of instances of this class are created with
 * different test data which are then used in the tests.
 */
@RunWith(Parameterized.class)
public class Vector2DUnaryTest
{
	public static final float ELIPSON_LENGTH = 0.00001f;
	public static final float ELIPSON_ANGLE  = 0.00000001f;

	@Parameterized.Parameter(value = 0)
	public float dataX;
	@Parameterized.Parameter(value = 1)
	public float dataY;
	@Parameterized.Parameter(value = 2)
	public float dataLength;
	@Parameterized.Parameter(value = 3)
	public float dataAngle;

	@Parameterized.Parameter(value = 4)
	public Vector2D vector;

	@Parameterized.Parameters
	public static Collection<Object[]> data()
	{
		// Generate data list
		float[][] data = new float[][]
		{
				// X, Y, Length, Angle
				{    0,    0,    0, 0 },
				{    3,    4,    5, (float) Math.atan(4.0 / 3.0) },
				{   -3,   -4,    5, (float) (Math.atan(4.0 / 3.0) - Math.PI) },
				{    3,   -4,    5, (float) -Math.atan(4.0 / 3.0) },
				{    0,  100,  100, (float) (Math.PI / 2) },
				{  100,    0,  100, 0 },
				{ -100,    0,  100, (float) -Math.PI },
		};

		// Generate test cases using normal + polar forms
		Collection<Object[]> result = new ArrayList<>();
		for (int i = 0; i < data.length; i++)
		{
			// Get and copy data
			float[] currentData = data[i];
			Object[] normal = floatToObjectArray(currentData, 5);
			Object[] polar = Arrays.copyOf(normal, 5);

			// Create vectors
			normal[4] = new Vector2D(currentData[0], currentData[1]);
			polar[4] = Vector2D.fromPolar(currentData[2], currentData[3]);

			// Add both to final result
			result.add(normal);
			result.add(polar);
		}

		return result;
	}

	/** Convert a float[] to an Object[] */
	private static Object[] floatToObjectArray(float[] input, int newLength)
	{
		Object[] result = new Object[newLength];

		for (int i = 0; i < input.length; i++)
			result[i] = input[i];

		return result;
	}

	@Test
	public void testXYValues()
	{
		assertThat(vector.getX(), closeTo(dataX, ELIPSON_LENGTH));
		assertThat(vector.getY(), closeTo(dataY, ELIPSON_LENGTH));
	}

	@Test
	public void testLength()
	{
		assertThat(vector.getLength(), closeTo(dataLength, ELIPSON_LENGTH));
	}

	@Test
	public void testLengthSquared()
	{
		assertThat(vector.getLengthSquared(), closeTo(dataLength * dataLength, ELIPSON_LENGTH));
	}

	@Test
	public void testAngle()
	{
		// For the zero vector, any angle is valid
		if (!vector.equals(Vector2D.ZERO))
			assertThat(vector.getAngle(), closeTo(dataAngle, ELIPSON_ANGLE));
	}
}
