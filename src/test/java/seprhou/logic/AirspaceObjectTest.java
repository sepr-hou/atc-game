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
 * Tests for {@link AirspaceObject}
 */
@RunWith(Enclosed.class)
public class AirspaceObjectTest
{
	@RunWith(JUnit4.class)
	public static class StaticTests
	{
		@Test
		public void testInitialValues()
		{
			AirspaceObject object = new AirspaceObjectMock();

			assertThat(object.getPosition(), is(equalTo(Vector2D.ZERO)));
			assertThat(object.getVelocity(), is(equalTo(Vector2D.ZERO)));
			assertThat(object.getAltitude(), is(equalTo(0f)));
			assertThat(object.getTargetVelocity(), is(equalTo(Vector2D.ZERO)));
			assertThat(object.getTargetAltitude(), is(equalTo(0f)));
		}
	}

	@RunWith(Parameterized.class)
	public static class RefreshTests
	{
		private final AirspaceObject object = new AirspaceObjectMock();

		public RefreshTests(Vector2D velocity, float altitude, Vector2D targetVelocity, float targetAltitude)
		{
			object.velocity = velocity;
			object.altitude = altitude;
			object.setTargetVelocity(targetVelocity);
			object.setTargetAltitude(targetAltitude);
		}

		@Parameterized.Parameters
		public static Collection<Object[]> data()
		{
			return Arrays.asList(new Object[][]
			{
				// Normal
				{ new Vector2D(10, 10), 1000, new Vector2D(50, 50), 5000 },

				// Extreme rates
				{ new Vector2D(10, 10), 1000, new Vector2D(-1000, -1000), -5000 },

				// No changes
				{ new Vector2D(10, 10), 1000, new Vector2D(10, 10), 1000 },
			});
		}

		private void doRefreshTest(float delta)
		{
			// Record current state
			float prevAltitude = object.getAltitude();
			Vector2D prevVelocity = object.getVelocity();
			float prevTargetAltitude = object.getTargetAltitude();
			Vector2D prevTargetVelocity = object.getTargetVelocity();

			// Refresh aircraft
			object.refresh(delta);

			// Compare to new state
			float altitude = object.getAltitude();
			Vector2D velocity = object.getVelocity();
			float targetAltitude = object.getTargetAltitude();
			Vector2D targetVelocity = object.getTargetVelocity();

			//  abs(altitude - targetAltitude) must be lower or 0
			assertThat(Math.abs(altitude - targetAltitude),
					is(either(closeTo(0)).or(lessThan(Math.abs(prevAltitude - prevTargetAltitude)))));

			//  altitude must be between min and max
			assertThat(altitude, is(between(object.getMinAltitude(), object.getMaxAltitude())));

			//  abs(speed - targetSpeed) must be lower of 0
			assertThat(Math.abs(velocity.getLength() - targetVelocity.getLength()),
					is(either(closeTo(0)).or(lessThan(Math.abs(prevVelocity.getLength() - prevTargetVelocity.getLength())))));

			//  speed must be between min and max
			assertThat(velocity.getLength(), is(between(object.getMinSpeed(), object.getMaxSpeed())));
		}

		@Test
		public void testRefreshSmall()
		{
			doRefreshTest(0.1f);
		}

		@Test
		public void testRefresh1()
		{
			doRefreshTest(1);
		}

		@Test
		public void testRefresh5()
		{
			doRefreshTest(5);
		}

		@Test
		public void testRefreshTwice()
		{
			object.refresh(1);
			doRefreshTest(1);
		}

		/** Inclusive between matcher */
		private static Matcher<Float> between(float min, float max)
		{
			return both(greaterThanOrEqualTo(min)).and(lessThanOrEqualTo(max));
		}
	}

	/** Fake {@link AirspaceObject} class used for testing */
	private static class AirspaceObjectMock extends AirspaceObject
	{
		@Override public void draw(Object state) { }
		@Override public float getSize() { return 64; }
		@Override public float getAscentRate() { return 100; }
		@Override public float getMinSpeed() { return 0; }
		@Override public float getMaxSpeed() { return 100; }
		@Override public float getMinAltitude() { return 0; }
		@Override public float getMaxAltitude() { return 5000; }
		@Override public float getMaxAcceleration() { return 100; }
		@Override public float getMaxTurnRate() { return 1; }
	}
}
