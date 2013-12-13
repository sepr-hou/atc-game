package seprhou.logic;

import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link Rectangle}
 */
@RunWith(Enclosed.class)
public class RectangleTest
{
	@RunWith(Parameterized.class)
	public static class ClassInvariant
	{
		private final Rectangle rectangle;
		private final float minX, minY, maxX, maxY;

		public ClassInvariant(Rectangle rectangle)
		{
			this.rectangle = rectangle;
			this.minX = Math.min(rectangle.getPoint1().getX(), rectangle.getPoint2().getX());
			this.minY = Math.min(rectangle.getPoint1().getY(), rectangle.getPoint2().getY());
			this.maxX = Math.max(rectangle.getPoint1().getX(), rectangle.getPoint2().getX());
			this.maxY = Math.max(rectangle.getPoint1().getY(), rectangle.getPoint2().getY());
		}

		/** Test data */
		@Parameterized.Parameters
		public static Collection<Object[]> data()
		{
			return Arrays.asList(new Object[][]
			{
				// Height + Width Test
				{ new Rectangle(50, 78) },

				// Test the 4 point ordering cases
				{ new Rectangle(new Vector2D(0, -9), new Vector2D(100, 100)) },
				{ new Rectangle(new Vector2D(100, 100), new Vector2D(-9, 0)) },
				{ new Rectangle(new Vector2D(-78, 100), new Vector2D(10, -9)) },
				{ new Rectangle(new Vector2D(10, -9), new Vector2D(-78, 100)) },

				// Test all negative
				{ new Rectangle(new Vector2D(-8, -9), new Vector2D(-78, -100)) },
			});
		}

		@Test
		public void testPointOrder()
		{
			assertThat(rectangle.getPoint1().getX(), is(minX));
			assertThat(rectangle.getPoint1().getY(), is(minY));
			assertThat(rectangle.getPoint2().getX(), is(maxX));
			assertThat(rectangle.getPoint2().getY(), is(maxY));
		}

		@Test
		public void testWidth()
		{
			assertThat(rectangle.getWidth(), is(maxX - minX));
		}

		@Test
		public void testHeight()
		{
			assertThat(rectangle.getHeight(), is(maxY - minY));
		}
	}

	@RunWith(JUnit4.class)
	public static class Intersections
	{
		@Test
		public void testContains()
		{
			//
		}

		@Test
		public void testIntersectsRect()
		{
			//
		}

		@Test
		public void testIntersectsCircle()
		{
			//
		}
	}
}
