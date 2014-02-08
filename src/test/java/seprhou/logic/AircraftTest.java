package seprhou.logic;

import java.util.Arrays;
import java.util.Collection;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.junit.runners.Parameterized;

/**
 * Tests for {@link Aircraft}
 */
@RunWith(Enclosed.class)
public class AircraftTest {
	@RunWith(Parameterized.class)
	public static class BearingTest {
		private final Aircraft aircraft;
		private final float correctBearing;

		public BearingTest(float correctBearing, double angle) {
			this.correctBearing = correctBearing;
			this.aircraft = new AircraftMock();
			this.aircraft.velocity = Vector2D.fromPolar(1, (float) angle);
		}

		@Parameterized.Parameters
		public static Collection<Object[]> data() {
			return Arrays.asList(new Object[][] { { 0, Math.PI / 2 }, { 90, 0 }, { 180, -Math.PI / 2 }, { 270, Math.PI }, { 270, -Math.PI }, { 359, Math.PI / 2 + Math.PI / 180 }, });
		}

		@Test
		public void testBearing() {
			Assert.assertThat(this.aircraft.getBearing(), Matchers.is(IsCloseToFloat.closeTo(this.correctBearing)));
		}
	}

	@RunWith(JUnit4.class)
	public static class WaypointCollisionTests {
		// Flight plan used for testing
		private static final FlightPlan FLIGHT_PLAN = new FlightPlan(Arrays.asList(new Vector2D(0, 0), new Vector2D(100, 100), new Vector2D(200, 200)), 0, 100, false, false);

		private Aircraft aircraft;

		@Before
		public void setupAircraft() {
			this.aircraft = new AircraftMock(WaypointCollisionTests.FLIGHT_PLAN);
		}

		@Test
		public void testInitialSetup() {
			Assert.assertThat(this.aircraft.getWaypointsHit(), Matchers.is(0));
			Assert.assertThat(this.aircraft.getLastWaypoint(), Matchers.is(0));
			this.aircraft.refresh(1);
			Assert.assertThat(this.aircraft.getWaypointsHit(), Matchers.is(0));
			Assert.assertThat(this.aircraft.getLastWaypoint(), Matchers.is(0));
		}

		@Test
		public void testNormalFlight() {
			// Miss a waypoint
			this.aircraft.position = new Vector2D(50, 50);
			this.aircraft.refresh(1);
			Assert.assertThat(this.aircraft.getWaypointsHit(), Matchers.is(0));
			Assert.assertThat(this.aircraft.getLastWaypoint(), Matchers.is(0));

			// Try to hit a waypoint
			this.aircraft.position = new Vector2D(80, 80);
			this.aircraft.refresh(1);
			Assert.assertThat(this.aircraft.getWaypointsHit(), Matchers.is(1));
			Assert.assertThat(this.aircraft.getLastWaypoint(), Matchers.is(1));

			// Waiting around should change nothing
			this.aircraft.refresh(1);
			Assert.assertThat(this.aircraft.getWaypointsHit(), Matchers.is(1));
			Assert.assertThat(this.aircraft.getLastWaypoint(), Matchers.is(1));

			// Hit the exit point
			this.aircraft.position = new Vector2D(200, 200);
			this.aircraft.refresh(1);
			Assert.assertThat(this.aircraft.getWaypointsHit(), Matchers.is(2));
			Assert.assertThat(this.aircraft.getLastWaypoint(), Matchers.is(2));
		}

		@Test
		public void testWaypointSkipping() {
			// Jump straight to exit point should have no effect
			this.aircraft.position = new Vector2D(200, 200);
			this.aircraft.refresh(1);
			Assert.assertThat(this.aircraft.getWaypointsHit(), Matchers.is(0));
			Assert.assertThat(this.aircraft.getLastWaypoint(), Matchers.is(0));

			// Jumping to next waypoint
			this.aircraft.position = new Vector2D(100, 100);
			this.aircraft.refresh(1);
			Assert.assertThat(this.aircraft.getWaypointsHit(), Matchers.is(1));
			Assert.assertThat(this.aircraft.getLastWaypoint(), Matchers.is(1));
		}
	}

	/** Fake {@link Aircraft} class used for testing */
	private static class AircraftMock extends Aircraft {
		public AircraftMock() {
			this(new FlightPlan(Arrays.asList(Vector2D.ZERO, Vector2D.ZERO), 0, 0, false, false));
		}

		public AircraftMock(FlightPlan flightPlan) {
			super("Test", 0, 0, flightPlan, 1000);
		}

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
	}
}
