package seprhou.logic;

import java.util.Arrays;
import java.util.Collection;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Tests for {@link CollisionWarning}
 */
@RunWith(Parameterized.class)
public class CollisionWarningTest {
	private final CollisionWarning warning, warningReverse;
	private final boolean crash;
	private final float lateral, vertical;

	public CollisionWarningTest(AirspaceObject a, AirspaceObject b,
			boolean crash, float lateral, float vertical) {
		this.warning = new CollisionWarning(a, b);
		this.warningReverse = new CollisionWarning(b, a);
		this.crash = crash;
		this.lateral = lateral;
		this.vertical = vertical;
	}

	/** Test data */
	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		return Arrays
				.asList(new Object[][] {
						// Normal flight
						{
								new AirspaceObjectMock(new Vector2D(100, 100),
										50, 100),
								new AirspaceObjectMock(new Vector2D(200, 200),
										30, 40), false,
								(float) (Math.sqrt(2) * 100), 20, },

						// Collided
						{
								new AirspaceObjectMock(new Vector2D(100, 100),
										50, 100),
								new AirspaceObjectMock(new Vector2D(120, 100),
										30, 100), true, 20, 20, }, });
	}

	@Test
	public void testHasCollided() {
		Assert.assertThat(this.warning.hasCollided(), Matchers.is(this.crash));
		Assert.assertThat(this.warningReverse.hasCollided(),
				Matchers.is(this.crash));
	}

	@Test
	public void testLateralDistance() {
		Assert.assertThat(this.warning.getLateralDistance(),
				IsCloseToFloat.closeTo(this.lateral));
		Assert.assertThat(this.warningReverse.getLateralDistance(),
				IsCloseToFloat.closeTo(this.lateral));
	}

	@Test
	public void testVerticalDistance() {
		Assert.assertThat(this.warning.getVerticalDistance(),
				IsCloseToFloat.closeTo(this.vertical));
		Assert.assertThat(this.warningReverse.getVerticalDistance(),
				IsCloseToFloat.closeTo(this.vertical));
	}

	/** Fake {@link AirspaceObject} class used for testing */
	private static class AirspaceObjectMock extends AirspaceObject {
		private final float size;

		public AirspaceObjectMock(Vector2D position, float altitude, float size) {
			this.position = position;
			this.altitude = altitude;
			this.size = size;
		}

		@Override
		public void draw(Object state) {}

		@Override
		public float getSize() {
			return this.size;
		}

		@Override
		public float getAscentRate() {
			return 0;
		}

		@Override
		public float getMinSpeed() {
			return 0;
		}

		@Override
		public float getMaxSpeed() {
			return 0;
		}

		@Override
		public float getMinAltitude() {
			return 0;
		}

		@Override
		public float getMaxAltitude() {
			return 0;
		}

		@Override
		public float getMaxAcceleration() {
			return 0;
		}

		@Override
		public float getMaxTurnRate() {
			return 0;
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
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public FlightPlan getFlightPlan() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public void setFlightPlan(FlightPlan flightPlan) {
			// TODO Auto-generated method stub

		}

		@Override
		public void resetRunwayPlane() {
			// TODO Auto-generated method stub

		}
	}
}
