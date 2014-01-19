package seprhou.logic;

import org.hamcrest.Matcher;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static seprhou.logic.IsCloseToFloat.closeTo;

/**
 * Tests for {@link Vector2D}
 */
@RunWith(Enclosed.class)
public class Vector2DTest
{
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
	public static class UnaryTests
	{
		private final float dataX, dataY, dataLength, dataAngle;
		private final Vector2D vector;

		public UnaryTests(boolean usePolar, float x, float y, float length, float angle)
		{
			this.dataX = x;
			this.dataY = y;
			this.dataLength = length;
			this.dataAngle = angle;

			if (usePolar)
				this.vector = Vector2D.fromPolar(length, angle);
			else
				this.vector = new Vector2D(x, y);
		}

		@Parameterized.Parameters
		public static Collection<Object[]> data()
		{
			return Arrays.asList(new Object[][]
			{
				// Use Polar, X, Y, Length, Angle
				{ true,     0,    0,    0, 0 },
				{ true,     3,    4,    5, (float) Math.atan(4.0 / 3.0) },
				{ true,    -3,   -4,    5, (float) (Math.atan(4.0 / 3.0) - Math.PI) },
				{ true,     3,   -4,    5, (float) -Math.atan(4.0 / 3.0) },
				{ true,     0,  100,  100, (float) (Math.PI / 2) },
				{ true,   100,    0,  100, 0 },
				{ true,  -100,    0,  100, (float) -Math.PI },

				{ false,    0,    0,    0, 0 },
				{ false,    3,    4,    5, (float) Math.atan(4.0 / 3.0) },
				{ false,   -3,   -4,    5, (float) (Math.atan(4.0 / 3.0) - Math.PI) },
				{ false,    3,   -4,    5, (float) -Math.atan(4.0 / 3.0) },
				{ false,    0,  100,  100, (float) (Math.PI / 2) },
				{ false,  100,    0,  100, 0 },
				{ false, -100,    0,  100, (float) -Math.PI },
			});
		}

		/** Returns a matcher which matches a value close to dataAngle */
		private Matcher<Float> closeToAngleMatcher()
		{
			Matcher<Float> matcher = closeTo(dataAngle);

			// If the angle is PI, allow +/- values for it
			if (dataAngle == (float) Math.PI || dataAngle == (float) -Math.PI)
				matcher = either(matcher).or(closeTo(-dataAngle));

			return matcher;
		}

		@Test
		public void testXYValues()
		{
			assertThat(vector.getX(), closeTo(dataX));
			assertThat(vector.getY(), closeTo(dataY));
		}

		@Test
		public void testLength()
		{
			assertThat(vector.getLength(), closeTo(dataLength));
		}

		@Test
		public void testLengthSquared()
		{
			assertThat(vector.getLengthSquared(), closeTo(dataLength * dataLength));
		}

		@Test
		public void testAngle()
		{
			// For the zero vector, any angle is valid
			if (!vector.equals(Vector2D.ZERO))
			{
				assertThat(vector.getAngle(), closeToAngleMatcher());
			}
		}

		@Test
		public void testNormalize()
		{
			Vector2D normalized = vector.normalize();

			assertThat(normalized.getLength(), closeTo(1));
			assertThat(normalized.getAngle(), closeToAngleMatcher());
		}

		@Test
		public void testChangeLength()
		{
			Vector2D newVector = vector.changeLength(50);

			assertThat(newVector.getLength(), closeTo(50));
			assertThat(newVector.getAngle(), closeToAngleMatcher());
		}

		@Test
		public void testEqualsNull()
		{
			assertThat(vector, not(equalTo(null)));
		}
	}

	/**
	 * Binary tests - ie tests which require two vectors as input
	 */
	@RunWith(JUnit4.class)
	public static class BinaryTests
	{
		@Test
		public void testDistanceTo()
		{
			float correctValue = (float) (10 * Math.sqrt(2));
			Vector2D a = new Vector2D(3, 4);
			Vector2D b = new Vector2D(5, -10);

			assertThat(a.distanceTo(b), closeTo(correctValue));
			assertThat(b.distanceTo(a), closeTo(correctValue));
		}

		@Test
		public void testAdd()
		{
			Vector2D a = new Vector2D(3, 4);
			Vector2D b = new Vector2D(5, -10);

			assertThat(a.add(b).getX(), closeTo( 8));
			assertThat(a.add(b).getY(), closeTo(-6));
			assertThat(b.add(a).getX(), closeTo( 8));
			assertThat(b.add(a).getY(), closeTo(-6));
		}

		@Test
		public void testSub()
		{
			Vector2D a = new Vector2D(3, 4);
			Vector2D b = new Vector2D(5, -10);

			assertThat(a.sub(b).getX(), closeTo( -2));
			assertThat(a.sub(b).getY(), closeTo( 14));
			assertThat(b.sub(a).getX(), closeTo(  2));
			assertThat(b.sub(a).getY(), closeTo(-14));
		}

		@Test
		public void testMultiply()
		{
			Vector2D a = new Vector2D(5, -10);
			float b = 42;

			assertThat(a.multiply(b).getX(), closeTo( 210));
			assertThat(a.multiply(b).getY(), closeTo(-420));
			assertThat(a.multiply(-b).getX(), closeTo(-210));
			assertThat(a.multiply(-b).getY(), closeTo( 420));
		}

		@Test
		public void testMultiplyZero()
		{
			Vector2D a = new Vector2D(5, -10);

			assertThat(a.multiply(0).getX(), closeTo(0));
			assertThat(a.multiply(0).getY(), closeTo(0));
		}

		@Test
		public void testRotate()
		{
			Vector2D a = new Vector2D(3, 4);
			float b = (float) (Math.PI / 4);

			float ansX = (float) (-1 / Math.sqrt(2));
			float ansY = (float) ( 7 / Math.sqrt(2));

			assertThat(a.rotate( b).getX(), closeTo(ansX));
			assertThat(a.rotate( b).getY(), closeTo(ansY));
			assertThat(a.rotate(-b).getX(), closeTo(ansY));
			assertThat(a.rotate(-b).getY(), closeTo(-ansX));
		}

		@Test
		public void testRotate2()
		{
			Vector2D a = Vector2D.XAXIS;
			float b = (float) (-Math.PI / 2);

			assertThat(a.rotate( b).getX(), closeTo(0));
			assertThat(a.rotate( b).getY(), closeTo(-1));
			assertThat(a.rotate(-b).getX(), closeTo(0));
			assertThat(a.rotate(-b).getY(), closeTo(1));
		}

		@Test
		public void testEquals()
		{
			Vector2D a = new Vector2D(3, 4);
			Vector2D b = new Vector2D(5, -10);
			Vector2D c = new Vector2D(3, 4);

			assertThat(a, is(a));
			assertThat(a, is(c));
			assertThat(a, not(b));
		}
	}
}
