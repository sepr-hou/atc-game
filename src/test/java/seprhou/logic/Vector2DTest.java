package seprhou.logic;

import java.util.Arrays;
import java.util.Collection;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;

/**
 * Tests for {@link Vector2D}
 */
@RunWith(Enclosed.class)
public class Vector2DTest {
	/**
	 * Contains the unary tests for {@link Vector2D}
	 * 
	 * <p>
	 * Unary tests are tests which apply a unary operator to the vector to get a
	 * value.
	 * 
	 * <p>
	 * This is a parameterized test, so lots of instances of this class are
	 * created with different test data which are then used in the tests.
	 */
	@RunWith(Parameterized.class)
	public static class UnaryTests {
		private final float dataX, dataY, dataLength, dataAngle;
		private final Vector2D vector;

		public UnaryTests(boolean usePolar, float x, float y, float length, double angleDouble) {
			float angle = (float) angleDouble;

			this.dataX = x;
			this.dataY = y;
			this.dataLength = length;
			this.dataAngle = angle;

			if (usePolar) {
				this.vector = Vector2D.fromPolar(length, angle);
			} else {
				this.vector = new Vector2D(x, y);
			}
		}

		@Parameterized.Parameters
		public static Collection<Object[]> data() {
			return Arrays.asList(new Object[][] {
					// Use Polar, X, Y, Length, Angle
					{ true, 0, 0, 0, 0 }, { true, 3, 4, 5, Math.atan(4.0 / 3.0) }, { true, -3, -4, 5, Math.atan(4.0 / 3.0) - Math.PI }, { true, 3, -4, 5, -Math.atan(4.0 / 3.0) }, { true, 0, 100, 100, Math.PI / 2 }, { true, 100, 0, 100, 0 }, { true, -100, 0, 100, -Math.PI },

					{ false, 0, 0, 0, 0 }, { false, 3, 4, 5, Math.atan(4.0 / 3.0) }, { false, -3, -4, 5, Math.atan(4.0 / 3.0) - Math.PI }, { false, 3, -4, 5, -Math.atan(4.0 / 3.0) }, { false, 0, 100, 100, Math.PI / 2 }, { false, 100, 0, 100, 0 }, { false, -100, 0, 100, -Math.PI }, });
		}

		/** Returns a matcher which matches a value close to dataAngle */
		private Matcher<Float> closeToAngleMatcher() {
			Matcher<Float> matcher = IsCloseToFloat.closeTo(this.dataAngle);

			// If the angle is PI, allow +/- values for it
			if (this.dataAngle == (float) Math.PI || this.dataAngle == (float) -Math.PI) {
				matcher = Matchers.either(matcher).or(IsCloseToFloat.closeTo(-this.dataAngle));
			}

			return matcher;
		}

		@Test
		public void testXYValues() {
			Assert.assertThat(this.vector.getX(), IsCloseToFloat.closeTo(this.dataX));
			Assert.assertThat(this.vector.getY(), IsCloseToFloat.closeTo(this.dataY));
		}

		@Test
		public void testLength() {
			Assert.assertThat(this.vector.getLength(), IsCloseToFloat.closeTo(this.dataLength));
		}

		@Test
		public void testLengthSquared() {
			Assert.assertThat(this.vector.getLengthSquared(), IsCloseToFloat.closeTo(this.dataLength * this.dataLength));
		}

		@Test
		public void testAngle() {
			// For the zero vector, any angle is valid
			if (!this.vector.equals(Vector2D.ZERO)) {
				Assert.assertThat(this.vector.getAngle(), this.closeToAngleMatcher());
			}
		}

		@Test
		public void testNormalize() {
			Vector2D normalized = this.vector.normalize();

			Assert.assertThat(normalized.getLength(), IsCloseToFloat.closeTo(1));
			Assert.assertThat(normalized.getAngle(), this.closeToAngleMatcher());
		}

		@Test
		public void testChangeLength() {
			Vector2D newVector = this.vector.changeLength(50);

			Assert.assertThat(newVector.getLength(), IsCloseToFloat.closeTo(50));
			Assert.assertThat(newVector.getAngle(), this.closeToAngleMatcher());
		}

		@Test
		public void testEqualsNull() {
			Assert.assertThat(this.vector, Matchers.not(Matchers.equalTo(null)));
		}
	}

	/**
	 * Binary tests - ie tests which require two vectors as input
	 */
	@RunWith(JUnit4.class)
	public static class BinaryTests {
		@Test
		public void testDistanceTo() {
			float correctValue = (float) (10 * Math.sqrt(2));
			Vector2D a = new Vector2D(3, 4);
			Vector2D b = new Vector2D(5, -10);

			Assert.assertThat(a.distanceTo(b), IsCloseToFloat.closeTo(correctValue));
			Assert.assertThat(b.distanceTo(a), IsCloseToFloat.closeTo(correctValue));
		}

		@Test
		public void testAdd() {
			Vector2D a = new Vector2D(3, 4);
			Vector2D b = new Vector2D(5, -10);

			Assert.assertThat(a.add(b).getX(), IsCloseToFloat.closeTo(8));
			Assert.assertThat(a.add(b).getY(), IsCloseToFloat.closeTo(-6));
			Assert.assertThat(b.add(a).getX(), IsCloseToFloat.closeTo(8));
			Assert.assertThat(b.add(a).getY(), IsCloseToFloat.closeTo(-6));
		}

		@Test
		public void testSub() {
			Vector2D a = new Vector2D(3, 4);
			Vector2D b = new Vector2D(5, -10);

			Assert.assertThat(a.sub(b).getX(), IsCloseToFloat.closeTo(-2));
			Assert.assertThat(a.sub(b).getY(), IsCloseToFloat.closeTo(14));
			Assert.assertThat(b.sub(a).getX(), IsCloseToFloat.closeTo(2));
			Assert.assertThat(b.sub(a).getY(), IsCloseToFloat.closeTo(-14));
		}

		@Test
		public void testMultiply() {
			Vector2D a = new Vector2D(5, -10);
			float b = 42;

			Assert.assertThat(a.multiply(b).getX(), IsCloseToFloat.closeTo(210));
			Assert.assertThat(a.multiply(b).getY(), IsCloseToFloat.closeTo(-420));
			Assert.assertThat(a.multiply(-b).getX(), IsCloseToFloat.closeTo(-210));
			Assert.assertThat(a.multiply(-b).getY(), IsCloseToFloat.closeTo(420));
		}

		@Test
		public void testMultiplyZero() {
			Vector2D a = new Vector2D(5, -10);

			Assert.assertThat(a.multiply(0).getX(), IsCloseToFloat.closeTo(0));
			Assert.assertThat(a.multiply(0).getY(), IsCloseToFloat.closeTo(0));
		}

		@Test
		public void testRotate() {
			Vector2D a = new Vector2D(3, 4);
			float b = (float) (Math.PI / 4);

			float ansX = (float) (-1 / Math.sqrt(2));
			float ansY = (float) (7 / Math.sqrt(2));

			Assert.assertThat(a.rotate(b).getX(), IsCloseToFloat.closeTo(ansX));
			Assert.assertThat(a.rotate(b).getY(), IsCloseToFloat.closeTo(ansY));
			Assert.assertThat(a.rotate(-b).getX(), IsCloseToFloat.closeTo(ansY));
			Assert.assertThat(a.rotate(-b).getY(), IsCloseToFloat.closeTo(-ansX));
		}

		@Test
		public void testRotate2() {
			Vector2D a = Vector2D.XAXIS;
			float b = (float) (-Math.PI / 2);

			Assert.assertThat(a.rotate(b).getX(), IsCloseToFloat.closeTo(0));
			Assert.assertThat(a.rotate(b).getY(), IsCloseToFloat.closeTo(-1));
			Assert.assertThat(a.rotate(-b).getX(), IsCloseToFloat.closeTo(0));
			Assert.assertThat(a.rotate(-b).getY(), IsCloseToFloat.closeTo(1));
		}

		@Test
		public void testEquals() {
			Vector2D a = new Vector2D(3, 4);
			Vector2D b = new Vector2D(5, -10);
			Vector2D c = new Vector2D(3, 4);

			Assert.assertThat(a, Matchers.is(a));
			Assert.assertThat(a, Matchers.is(c));
			Assert.assertThat(a, Matchers.not(b));
		}
	}
}
