package seprhou.logic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static seprhou.logic.IsCloseToFloat.closeTo;

/**
 * Tests for {@link CollisionWarning}
 */
@RunWith(Parameterized.class)
public class CollisionWarningTest
{
	private final CollisionWarning warning, warningReverse;
	private final boolean crash;
	private final float lateral, vertical;

	public CollisionWarningTest(AirspaceObject a, AirspaceObject b, boolean crash, float lateral, float vertical)
	{
		this.warning = new CollisionWarning(a, b);
		this.warningReverse = new CollisionWarning(b, a);
		this.crash = crash;
		this.lateral = lateral;
		this.vertical = vertical;
	}

	/** Test data */
	@Parameterized.Parameters
	public static Collection<Object[]> data()
	{
		return Arrays.asList(new Object[][]
		{
			// Normal flight
			{
				new AirspaceObjectMock(new Vector2D(100, 100), 50, 100),
				new AirspaceObjectMock(new Vector2D(200, 200), 30, 40),
				false,
				(float) (Math.sqrt(2) * 100),
				20,
			},

			// Collided
			{
				new AirspaceObjectMock(new Vector2D(100, 100), 50, 100),
				new AirspaceObjectMock(new Vector2D(120, 100), 30, 100),
				true,
				20,
				20,
			},
		});
	}

	@Test
	public void testHasCollided()
	{
		assertThat(warning.hasCollided(), is(crash));
		assertThat(warningReverse.hasCollided(), is(crash));
	}

	@Test
	public void testLateralDistance()
	{
		assertThat(warning.getLateralDistance(), closeTo(lateral));
		assertThat(warningReverse.getLateralDistance(), closeTo(lateral));
	}

	@Test
	public void testVerticalDistance()
	{
		assertThat(warning.getVerticalDistance(), closeTo(vertical));
		assertThat(warningReverse.getVerticalDistance(), closeTo(vertical));
	}

	/** Fake {@link AirspaceObject} class used for testing */
	private static class AirspaceObjectMock extends AirspaceObject
	{
		private final float size;

		public AirspaceObjectMock(Vector2D position, float altitude, float size)
		{
			this.position = position;
			this.altitude = altitude;
			this.size = size;
		}

		@Override public void draw(Object state) { }
		@Override public float getSize() { return size; }
		@Override public float getAscentRate() { return 0; }
		@Override public float getMinSpeed() { return 0; }
		@Override public float getMaxSpeed() { return 0; }
		@Override public float getMinAltitude() { return 0; }
		@Override public float getMaxAltitude() { return 0; }
		@Override public float getMaxAcceleration() { return 0; }
		@Override public float getMaxTurnRate() { return 0; }

		@Override public boolean isFinished() { return false; }
		@Override public boolean isViolated() { return false; }
		@Override public int getScore() { return 0; }
		@Override public FlightPlan getFlightPlan() { return null; }
		@Override public void setFlightPlan(FlightPlan flightPlan) { }
		@Override public void resetRunwayPlane() { }
		@Override public void setViolated(boolean value) { }
	}
}
