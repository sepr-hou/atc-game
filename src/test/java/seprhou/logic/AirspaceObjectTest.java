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
 * Tests for {@link AirspaceObject}
 */
@RunWith(Enclosed.class)
public class AirspaceObjectTest {
	@RunWith(JUnit4.class)
	public static class StaticTests {
		@Test
		public void testInitialValues() {
			AirspaceObject object = new AirspaceObjectMock();

			Assert.assertThat(object.getPosition(), Matchers.is(Matchers.equalTo(Vector2D.ZERO)));
			Assert.assertThat(object.getVelocity(), Matchers.is(Matchers.equalTo(Vector2D.ZERO)));
			Assert.assertThat(object.getAltitude(), Matchers.is(Matchers.equalTo(0f)));
			Assert.assertThat(object.getTargetVelocity(), Matchers.is(Matchers.equalTo(Vector2D.ZERO)));
			Assert.assertThat(object.getTargetAltitude(), Matchers.is(Matchers.equalTo(0f)));
		}
	}

	@RunWith(Parameterized.class)
	public static class RefreshTests {
		private final AirspaceObject object = new AirspaceObjectMock();

		public RefreshTests(Vector2D velocity, float altitude, Vector2D targetVelocity, float targetAltitude) {
			this.object.velocity = velocity;
			this.object.altitude = altitude;
			this.object.setTargetVelocity(targetVelocity);
			this.object.setTargetAltitude(targetAltitude);
		}

		@Parameterized.Parameters
		public static Collection<Object[]> data() {
			return Arrays.asList(new Object[][] {
					// Normal
					{ new Vector2D(10, 10), 1000, new Vector2D(50, 50), 5000 },

					// Extreme rates
					{ new Vector2D(10, 10), 1000, new Vector2D(-1000, -1000), -5000 },

					// No changes
					{ new Vector2D(10, 10), 1000, new Vector2D(10, 10), 1000 }, });
		}

		private void doRefreshTest(float delta) {
			// Record current state
			float prevAltitude = this.object.getAltitude();
			Vector2D prevVelocity = this.object.getVelocity();
			float prevTargetAltitude = this.object.getTargetAltitude();
			Vector2D prevTargetVelocity = this.object.getTargetVelocity();

			// Refresh aircraft
			this.object.refresh(delta);

			// Compare to new state
			float altitude = this.object.getAltitude();
			Vector2D velocity = this.object.getVelocity();
			float targetAltitude = this.object.getTargetAltitude();
			Vector2D targetVelocity = this.object.getTargetVelocity();

			// abs(altitude - targetAltitude) must be lower or 0
			Assert.assertThat(Math.abs(altitude - targetAltitude), Matchers.is(Matchers.either(IsCloseToFloat.closeTo(0)).or(Matchers.lessThan(Math.abs(prevAltitude - prevTargetAltitude)))));

			// altitude must be between min and max
			Assert.assertThat(altitude, Matchers.is(RefreshTests.between(this.object.getMinAltitude(), this.object.getMaxAltitude())));

			// abs(speed - targetSpeed) must be lower of 0
			Assert.assertThat(Math.abs(velocity.getLength() - targetVelocity.getLength()), Matchers.is(Matchers.either(IsCloseToFloat.closeTo(0)).or(Matchers.lessThan(Math.abs(prevVelocity.getLength() - prevTargetVelocity.getLength())))));

			// speed must be between min and max
			Assert.assertThat(velocity.getLength(), Matchers.is(RefreshTests.between(this.object.getMinSpeed(), this.object.getMaxSpeed())));
		}

		@Test
		public void testRefreshSmall() {
			this.doRefreshTest(0.1f);
		}

		@Test
		public void testRefresh1() {
			this.doRefreshTest(1);
		}

		@Test
		public void testRefresh5() {
			this.doRefreshTest(5);
		}

		@Test
		public void testRefreshTwice() {
			this.object.refresh(1);
			this.doRefreshTest(1);
		}

		/** Inclusive between matcher */
		private static Matcher<Float> between(float min, float max) {
			return Matchers.both(Matchers.greaterThanOrEqualTo(min)).and(Matchers.lessThanOrEqualTo(max));
		}
	}

	/** Fake {@link AirspaceObject} class used for testing */
	private static class AirspaceObjectMock extends AirspaceObject {
		@Override
		public void draw(Object state) {}

		@Override
		public float getSize() {
			return 64;
		}

		@Override
		public float getAscentRate() {
			return 100;
		}

		@Override
		public float getMinSpeed() {
			return 0;
		}

		@Override
		public float getMaxSpeed() {
			return 100;
		}

		@Override
		public float getMinAltitude() {
			return 0;
		}

		@Override
		public float getMaxAltitude() {
			return 5000;
		}

		@Override
		public float getMaxAcceleration() {
			return 100;
		}

		@Override
		public float getMaxTurnRate() {
			return 1;
		}

		@Override
		public void setViolated(boolean value) {}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isViolated() {
			return false;
		}
		
		@Override
		public int getScore() {
			return 100;
		}
	}
}
