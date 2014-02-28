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

/**
 * Tests for {@link FlightPlanGenerator}
 *
 * <p>
 * Currently only tests that the resulting flight plans are valid
 */
@RunWith(Parameterized.class)
public class FlightPlanGeneratorTest
{
	// Some lists used for testing
	private static final List<Float> SPEEDS = Arrays.asList(20f, 30f, 60f, 80f);

	private static final List<Float> ALTITUDES = Arrays.asList(120f, 130f, 160f, 180f);

	private static final List<Vector2D> WAYPOINTS = Arrays.asList(
			new Vector2D(280, 210),
			new Vector2D(280, 420),
			new Vector2D(280, 630),
			new Vector2D(280, 840),
			new Vector2D(560, 210),
			new Vector2D(560, 420),
			new Vector2D(560, 630),
			new Vector2D(560, 840),
			new Vector2D(840, 210),
			new Vector2D(840, 420)
	);

	private static final List<Vector2D> ENTRY_EXIT_POINTS = Arrays.asList(
			new Vector2D(100, 0),
			new Vector2D(0, 800),
			new Vector2D(1000, 0)
	);

	private static final List<Runway> RUNWAYS = Arrays.asList(
			new Runway(new Vector2D(766.5f, 682.5f), new Vector2D(1018.5f, 426.3f)),
			new Runway(new Vector2D(722.4f, 508.2f), new Vector2D(877.8f, 663.6f))
	);

	private static final int MIN_WAYPOINTS = 0;
	private static final int MAX_WAYPOINTS = 4;

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
		generator.setRunways(FlightPlanGeneratorTest.RUNWAYS);
		generator.setWaypoints(WAYPOINTS);
		generator.setEntryExitPoints(ENTRY_EXIT_POINTS);
		generator.setInitialSpeeds(SPEEDS);
		generator.setInitialAltitudes(ALTITUDES);
		generator.setMinWaypoints(MIN_WAYPOINTS);
		generator.setMaxWaypoints(MAX_WAYPOINTS);
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
		assertThat(plan.getInitialSpeed(), isIn(SPEEDS));
		assertThat(plan.getInitialAltitude(), isIn(ALTITUDES));

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
		@Override
		public Collection<AirspaceObject> getActiveObjects()
		{
			return Collections.emptyList();
		}
	}
}
