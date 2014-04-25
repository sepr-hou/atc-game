package seprhou.logic;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static seprhou.logic.IsDistinct.distinct;
import static seprhou.logic.LogicConstants.*;

/**
 * Tests for {@link FlightPlanGenerator}
 *
 * <p>
 * Currently only tests that the resulting flight plans are valid
 */
@RunWith(Parameterized.class)
public class FlightPlanGeneratorTest
{
	// The generator
	private FlightPlanGenerator generator;

	@Parameterized.Parameters
	public static Collection<Object[]> data()
	{
		// Run tests 10 times
		return Arrays.asList(new Object[10][0]);
	}

	@Before
	public void resetGenerator()
	{
		generator = new FlightPlanGenerator();
	}

	@Test
	public void testNoAirspaceModifications()
	{
		Airspace airspace = new DummyAirspace();
		generator.makeFlightPlanNow(airspace);

		assertThat(airspace.getActiveObjects(), hasSize(0));
	}

	@Test
	public void testFlightPlan()
	{
		FlightPlan flightPlan = generator.makeFlightPlanNow(new DummyAirspace(), false);

		// Validate returned flight plan
		assertThat(flightPlan, is(notNullValue()));
		validateFlightPlan(flightPlan);
	}

	/** Validates a flight plan against the generator's configuration */
	private void validateFlightPlan(FlightPlan plan)
	{
		List<Vector2D> waypoints = plan.getWaypoints();

		// Initial speed + altitude
		assertThat(plan.getInitialSpeed(), isIn(INITIAL_SPEEDS));
		assertThat(plan.getInitialAltitude(), isIn(INITIAL_ALTITUDES));

		// Size of waypoints list
		Matcher<Integer> sizeMatcher =
				both(greaterThanOrEqualTo(MIN_WAYPOINTS + 2))
				.and(lessThanOrEqualTo(MAX_WAYPOINTS + 2));

		assertThat(waypoints, hasSize(sizeMatcher));

		// All points must be distinct
		assertThat(waypoints, is(distinct()));

		// Waypoints must be chosen from the correct lists
		assertThat(waypoints.get(0), isIn(ENTRY_EXIT_POINTS));
		assertThat(waypoints.get(waypoints.size() - 1), isIn(ENTRY_EXIT_POINTS));
		assertThat(waypoints.subList(1, waypoints.size() - 1), everyItem(isIn(WAYPOINTS)));
	}

	/** Airspace used to feed the current number of aircraft to the flight path generator */
	private static class DummyAirspace extends Airspace
	{
		public DummyAirspace()
		{
			super(Rectangle.ZERO, null);
		}

		@Override
		public Collection<AirspaceObject> getActiveObjects()
		{
			return Collections.emptyList();
		}
	}
}
