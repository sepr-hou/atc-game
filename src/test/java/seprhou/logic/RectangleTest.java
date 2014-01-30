package seprhou.logic;

import java.util.Arrays;
import java.util.Collection;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Tests for {@link Rectangle}
 */
@RunWith(Enclosed.class)
public class RectangleTest {
	private static final Rectangle TEST_RECTANGLE = new Rectangle(10, 10);

	@RunWith(Parameterized.class)
	public static class ClassInvariant {
		private final Rectangle rectangle;
		private final float minX, minY, maxX, maxY;

		public ClassInvariant(Rectangle rectangle) {
			this.rectangle = rectangle;
			this.minX = Math.min(rectangle.getPoint1().getX(), rectangle.getPoint2().getX());
			this.minY = Math.min(rectangle.getPoint1().getY(), rectangle.getPoint2().getY());
			this.maxX = Math.max(rectangle.getPoint1().getX(), rectangle.getPoint2().getX());
			this.maxY = Math.max(rectangle.getPoint1().getY(), rectangle.getPoint2().getY());
		}

		/** Test data */
		@Parameterized.Parameters
		public static Collection<Object[]> data() {
			return Arrays.asList(new Object[][] {
					// Height + Width Test
					{ new Rectangle(50, 78) },

					// Test the 4 point ordering cases
					{ new Rectangle(new Vector2D(0, -9), new Vector2D(100, 100)) }, { new Rectangle(new Vector2D(100, 100), new Vector2D(-9, 0)) }, { new Rectangle(new Vector2D(-78, 100), new Vector2D(10, -9)) }, { new Rectangle(new Vector2D(10, -9), new Vector2D(-78, 100)) },

					// Test all negative
					{ new Rectangle(new Vector2D(-8, -9), new Vector2D(-78, -100)) }, });
		}

		@Test
		public void testPointOrder() {
			Assert.assertThat(this.rectangle.getPoint1().getX(), Matchers.is(this.minX));
			Assert.assertThat(this.rectangle.getPoint1().getY(), Matchers.is(this.minY));
			Assert.assertThat(this.rectangle.getPoint2().getX(), Matchers.is(this.maxX));
			Assert.assertThat(this.rectangle.getPoint2().getY(), Matchers.is(this.maxY));
		}

		@Test
		public void testWidth() {
			Assert.assertThat(this.rectangle.getWidth(), Matchers.is(this.maxX - this.minX));
		}

		@Test
		public void testHeight() {
			Assert.assertThat(this.rectangle.getHeight(), Matchers.is(this.maxY - this.minY));
		}
	}

	@RunWith(Parameterized.class)
	public static class Contains {
		private static final Rectangle CONTAINS_TEST_RECTANGLE = new Rectangle(new Vector2D(-10, -10), new Vector2D(10, 10));

		private final Vector2D testVector;
		private final boolean pass;

		public Contains(boolean pass, Vector2D testVector) {
			this.testVector = testVector;
			this.pass = pass;
		}

		/** Test data */
		@Parameterized.Parameters
		public static Collection<Object[]> data() {
			return Arrays.asList(new Object[][] {
					// Passing tests
					{ true, Vector2D.ZERO }, { true, new Vector2D(-10, -10) }, { true, new Vector2D(9, -10) }, { true, new Vector2D(-10, 9) },

					// Failing tests
					{ false, new Vector2D(1000, 1000) }, { false, new Vector2D(11, 11) }, { false, new Vector2D(0, 11) }, { false, new Vector2D(-10.0001f, -10) }, });
		}

		@Test
		public void containsTest() {
			Assert.assertThat(this.testVector.toString(), Contains.CONTAINS_TEST_RECTANGLE.contains(this.testVector), Matchers.is(this.pass));
		}
	}

	@RunWith(Parameterized.class)
	public static class IntersectsRectangle {
		private final Rectangle rectangle2;
		private final boolean pass;

		public IntersectsRectangle(boolean pass, Rectangle rectangle2) {
			this.rectangle2 = rectangle2;
			this.pass = pass;
		}

		/** Test data */
		@Parameterized.Parameters
		public static Collection<Object[]> data() {
			return Arrays.asList(new Object[][] {
					// Passing tests
					{ true, new Rectangle(100, 100) }, { true, new Rectangle(new Vector2D(1, 1), new Vector2D(9, 9)) }, { true, new Rectangle(new Vector2D(-10, -10), new Vector2D(1, 1)) }, { true, new Rectangle(new Vector2D(9, 0), new Vector2D(10, 10)) },

					// Failing tests
					{ false, new Rectangle(new Vector2D(-10, -10), new Vector2D(-5, -5)) }, { false, new Rectangle(new Vector2D(-10, -10), new Vector2D(0, 0)) }, { false, new Rectangle(Vector2D.ZERO, Vector2D.ZERO) }, { false, new Rectangle(new Vector2D(10, 10), new Vector2D(15, 15)) }, { false, new Rectangle(new Vector2D(0, 0), new Vector2D(-5, 5)) }, });
		}

		@Test
		public void intersectsTest() {
			Assert.assertThat(this.rectangle2.toString(), RectangleTest.TEST_RECTANGLE.intersects(this.rectangle2), Matchers.is(this.pass));
		}

		@Test
		public void intersectsTestReverse() {
			Assert.assertThat(this.rectangle2.toString(), this.rectangle2.intersects(RectangleTest.TEST_RECTANGLE), Matchers.is(this.pass));
		}
	}

	@RunWith(Parameterized.class)
	public static class IntersectsCircle {
		private final Vector2D center;
		private final float radius;
		private final boolean pass;

		public IntersectsCircle(boolean pass, Vector2D center, float radius) {
			this.center = center;
			this.radius = radius;
			this.pass = pass;
		}

		/** Test data */
		@Parameterized.Parameters
		public static Collection<Object[]> data() {
			return Arrays.asList(new Object[][] {
					// Passing tests
					{ true, new Vector2D(0, 0), 1000 }, { true, new Vector2D(0, 0), 1 }, { true, new Vector2D(5, 5), 1 }, { true, new Vector2D(11, 11), 2 }, { true, new Vector2D(5, 11), 2 }, { true, new Vector2D(5, 11), 6 }, { true, new Vector2D(10, 11), 2 },

					// Failing tests
					{ false, new Vector2D(1000, 1000), 10 }, { false, new Vector2D(11, 11), 1 }, { false, new Vector2D(10, 11), 0.999f }, });
		}

		@Test
		public void intersectsTest() {
			Assert.assertThat("Other Circle: (" + this.center.getX() + ", " + this.center.getY() + ") r = " + this.radius, RectangleTest.TEST_RECTANGLE.intersects(this.center, this.radius), Matchers.is(this.pass));
		}
	}
}
