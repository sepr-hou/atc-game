package seprhou.logic;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

/**
 * Tests for {@link FlightPlanGenerator}
 * 
 * <p>
 * Currently only tests that the resulting flight plans are valid
 */
@RunWith(Parameterized.class)
public class FlightPlanGeneratorTest {
	// Some lists used for testing
	private static final List<Float> SPEEDS = Arrays.asList(20f, 30f, 60f, 80f);
	private static final List<Float> ALTITUDES = Arrays.asList(120f, 130f,
			160f, 180f);

	private static final List<Vector2D> WAYPOINTS = Arrays.asList(new Vector2D(
			280, 210), new Vector2D(280, 420), new Vector2D(280, 630),
			new Vector2D(280, 840), new Vector2D(560, 210), new Vector2D(560,
					420), new Vector2D(560, 630), new Vector2D(560, 840),
			new Vector2D(840, 210), new Vector2D(840, 420));

	private static final List<Vector2D> ENTRY_EXIT_POINTS = Arrays.asList(
			new Vector2D(100, 0), new Vector2D(0, 800), new Vector2D(1000, 0));
	private static final List<Runway> RUNWAYS = Arrays.asList(new Runway(
			new Vector2D(766.5f, 682.5f), new Vector2D(1018.5f, 426.3f)),
			new Runway(new Vector2D(722.4f, 508.2f), new Vector2D(877.8f,
					663.6f)));

	private static final int MIN_WAYPOINTS = 0;
	private static final int MAX_WAYPOINTS = 4;

	// The generator
	private FlightPlanGenerator generator;

	@Parameterized.Parameters
	public static Collection<Object[]> data() {
		// Run tests 10 times
		return Arrays.asList(new Object[10][0]);
	}

	@Before
	public void resetGenerator() {
		this.generator = new FlightPlanGenerator();
		this.generator.setRunways(FlightPlanGeneratorTest.RUNWAYS);
		this.generator.setWaypoints(FlightPlanGeneratorTest.WAYPOINTS);
		this.generator
				.setEntryExitPoints(FlightPlanGeneratorTest.ENTRY_EXIT_POINTS);
		this.generator.setInitialSpeeds(FlightPlanGeneratorTest.SPEEDS);
		this.generator.setInitialAltitudes(FlightPlanGeneratorTest.ALTITUDES);
		this.generator.setMinWaypoints(FlightPlanGeneratorTest.MIN_WAYPOINTS);
		this.generator.setMaxWaypoints(FlightPlanGeneratorTest.MAX_WAYPOINTS);
	}

	@Test
	public void testNoAirspaceModifications() {
		Airspace airspace = new DummyAirspace();
		this.generator.makeFlightPlanNow(airspace);

		Assert.assertThat(airspace.getActiveObjects(), Matchers.hasSize(0));
	}

	@Test
	public void testFlightPlan() {
		FlightPlan flightPlan = this.generator.makeFlightPlanNow(
				new DummyAirspace(), false);

		// Validate returned flight plan
		Assert.assertThat(flightPlan, Matchers.is(Matchers.notNullValue()));
		this.validateFlightPlan(flightPlan);
	}

	/** Validates a flight plan against the generator's configuration */
	private void validateFlightPlan(FlightPlan plan) {
		List<Vector2D> waypoints = plan.getWaypoints();

		// Initial speed + altitude
		Assert.assertThat(plan.getInitialSpeed(),
				Matchers.isIn(FlightPlanGeneratorTest.SPEEDS));
		Assert.assertThat(plan.getInitialAltitude(),
				Matchers.isIn(FlightPlanGeneratorTest.ALTITUDES));

		// Size of waypoints list
		Matcher<Integer> sizeMatcher = Matchers
				.both(Matchers
						.greaterThanOrEqualTo(FlightPlanGeneratorTest.MIN_WAYPOINTS + 2))
				.and(Matchers
						.lessThanOrEqualTo(FlightPlanGeneratorTest.MAX_WAYPOINTS + 2));

		Assert.assertThat(waypoints, Matchers.hasSize(sizeMatcher));

		// All points must be distinct
		Assert.assertThat(waypoints, Matchers.is(IsDistinct.distinct()));

		// Waypoints must be chosen from the correct lists
		Assert.assertThat(waypoints.get(0),
				Matchers.isIn(FlightPlanGeneratorTest.ENTRY_EXIT_POINTS));
		Assert.assertThat(waypoints.get(waypoints.size() - 1),
				Matchers.isIn(FlightPlanGeneratorTest.ENTRY_EXIT_POINTS));
		Assert.assertThat(waypoints.subList(1, waypoints.size() - 1), Matchers
				.everyItem(Matchers.isIn(FlightPlanGeneratorTest.WAYPOINTS)));
	}

	/**
	 * Airspace used to feed the current number of aircraft to the flight path
	 * generator
	 */
	private static class DummyAirspace extends Airspace {
		@Override
		public Collection<AirspaceObject> getActiveObjects() {
			return Collections.emptyList();
		}
	}
}
