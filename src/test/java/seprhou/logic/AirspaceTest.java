package seprhou.logic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.*;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link Airspace}
 */
@RunWith(JUnit4.class)
public class AirspaceTest
{
	private static final Rectangle DIMENSIONS = new Rectangle(1000, 1000);
	private static final float SEPARATION = 200;

	@Test
	public void testObjectCulling()
	{
		Vector2D objectOk = new Vector2D(100, 100);
		Vector2D objectCulled = new Vector2D(2000, 100);
		Airspace airspace = generateAirspace(objectOk, objectCulled);

		// Validate state before refresh
		assertThat(airspace.getCulledObjects(), hasSize(0));
		assertThat(airspace.getActiveObjects(), hasSize(2));

		// After one iteration
		airspace.refresh(1);
		assertThat(airspace.getCulledObjects(), hasSize(1));
		assertThat(airspace.getActiveObjects(), hasSize(1));

		// Culled object is purged after another iteration
		airspace.refresh(1);
		assertThat(airspace.getCulledObjects(), hasSize(0));
		assertThat(airspace.getActiveObjects(), hasSize(1));
	}

	@Test
	public void testActiveObjectsOrder()
	{
		Airspace airspace = generateAirspace(1, 4, 8, 0, 4);
		airspace.refresh(1);

		// Ensure objects are in order
		Collection<AirspaceObject> activeObjects = airspace.getActiveObjects();
		assertThat(activeObjects, hasSize(5));

		List<Float> altitudes = new ArrayList<>();
		for (AirspaceObject object : activeObjects)
			altitudes.add(object.getAltitude());

		assertThat(altitudes, contains(0f, 1f, 4f, 4f, 8f));
	}

	@Test
	public void testCollisionWarningsAltitude()
	{
		Airspace airspace = generateAirspace(100, 400, 800, 0, 400);
		airspace.refresh(1);

		/*
		Warnings between:
		 0 - 100
		 400 - 400
		 */

		// Test against actual collision warnings
		Collection<CollisionWarning> warnings = airspace.getCollisionWarnings();
		assertThat(warnings, hasSize(2));

		for (CollisionWarning warning : warnings)
		{
			// Check which warning it is
			if (warning.getObject1().getAltitude() == 400)
			{
				assertThat(warning.getObject2().getAltitude(), is(400f));
			}
			else if (warning.getObject1().getAltitude() == 100)
			{
				assertThat(warning.getObject2().getAltitude(), is(0f));
			}
			else
			{
				assertThat(warning.getObject1().getAltitude(), is(0f));
				assertThat(warning.getObject2().getAltitude(), is(100f));
			}
		}
	}

	@Test
	public void testGameOver()
	{
		Airspace airspace1 = generateAirspace(1, 5000);
		Airspace airspace2 = generateAirspace(1, 2);

		// Airspace 1 is bad, airspace 2 is good
		airspace1.refresh(1);
		airspace2.refresh(1);
		assertThat(airspace1.isGameOver(), is(false));
		assertThat(airspace2.isGameOver(), is(true));
	}

	@Test
	public void testFindAircraft()
	{
		Vector2D obj1 = new Vector2D(100, 100);
		Vector2D obj2 = new Vector2D(200, 200);
		Vector2D obj3 = new Vector2D(300, 300);
		Airspace airspace = generateAirspace(obj1, obj2, obj3);
		airspace.refresh(1);

		// Test find aircraft clicking
		AirspaceObject found = airspace.findAircraft(obj2);
		assertThat(found, is(notNullValue()));
		assertThat(found.getPosition(), is(obj2));

		// Test none in range
		AirspaceObject found2 = airspace.findAircraft(new Vector2D(5000, 400));
		assertThat(found2, is(nullValue()));

		// Test circle
		List<Vector2D> positions = objectsToPositions(airspace.findAircraft(obj3, 200));
		assertThat(positions, containsInAnyOrder(obj2, obj3));
	}

	@Test
	public void testFindAircraftAltitude()
	{
		Airspace airspace = generateAirspace(1, 2, 3);
		airspace.refresh(1);

		// Aircraft search should return 3
		AirspaceObject found = airspace.findAircraft(Vector2D.ZERO);
		assertThat(found, is(notNullValue()));
		assertThat(found.getAltitude(), is(3f));
	}

	@Test
	public void testCycleAircraft() {
		Airspace airspace = AirspaceTest.generateAirspace(2);
		airspace.cycleAircraft();
		AirspaceObject obj1 = airspace.cycleAircraft();
		AirspaceObject obj2 = airspace.cycleAircraft();

		assertThat(obj1, equalTo(airspace.cycleAircraft()));
		assertThat(obj2, equalTo(airspace.cycleAircraft()));
	}

	@Test
	public void testTakeOffLanding()
	{
		// Prepare airspace
		Airspace airspace = generateAirspace(0, true);
		AirspaceObjectMock plane = new AirspaceObjectMock();
		airspace.getActiveObjects().add(plane);

		// Plane should still be active after one turn
		airspace.refresh(1);
		assertThat(airspace.getActiveObjects(), hasItem(plane));
		assertThat(airspace.getLandedObjects(), is(0));
		assertThat(airspace.getLandingPlanes(), is(0));

		// Force the plane to land (by marking it as finished)
		plane.finished = true;
		plane.flightPlan = makeFakeFlightPlan(true);
		airspace.setLandingPlanes(1);
		airspace.refresh(1);
		assertThat(airspace.getActiveObjects(), empty());
		assertThat(airspace.getLandedObjects(), is(1));
		assertThat(airspace.getLandingPlanes(), is(0));

		// Make plane takeoff (should generate a completely separate object)
		airspace.takeOff();
		airspace.refresh(1);
		assertThat(airspace.getActiveObjects(), hasSize(1));
		assertThat(airspace.getActiveObjects(), not(hasItem(plane)));
		assertThat(airspace.getLandedObjects(), is(0));
		assertThat(airspace.getLandingPlanes(), is(0));
	}

	/** Converts a list of objects to a list of positions */
	private static List<Vector2D> objectsToPositions(Iterable<AirspaceObject> objects)
	{
		List<Vector2D> positions = new ArrayList<>();

		for (AirspaceObject object : objects)
			positions.add(object.getPosition());

		return positions;
	}

	/** Create a fake flight plan */
	private static FlightPlan makeFakeFlightPlan(boolean landing)
	{
		return new FlightPlan(Arrays.asList(Vector2D.ZERO, Vector2D.ZERO), 0, 0, landing, !landing);
	}

	/** Generates an airspace with the given number of objects */
	private static Airspace generateAirspace(int objectCount)
	{
		return generateAirspace(objectCount, false);
	}

	/** Generates an airspace with the given number of objects */
	private static Airspace generateAirspace(int objectCount, boolean useFactory)
	{
		// Make factory
		AirspaceObjectFactory factory = null;
		if (useFactory)
			factory = new AirspaceObjectFactoryImpl();

		// Configure airspace
		Airspace airspace = new Airspace(DIMENSIONS, factory);
		airspace.setLateralSeparation(SEPARATION);
		airspace.setVerticalSeparation(SEPARATION);
		airspace.setFlightPlanGenerator(new FlightPathGeneratorMock());

		// Generate some objects
		for (int i = 0; i < objectCount; i++)
			airspace.getActiveObjects().add(new AirspaceObjectMock());

		// The game should not be over
		assertThat(airspace.isGameOver(), is(false));
		return airspace;
	}

	/** Generates an airspace with some objects with the given positions */
	private static Airspace generateAirspace(Vector2D... objects)
	{
		Airspace airspace = generateAirspace(objects.length);

		// Set their positions
		int i = 0;
		for (AirspaceObject object : airspace.getActiveObjects())
		{
			object.position = objects[i];
			i++;
		}

		return airspace;
	}

	/** Generates an airspace with some objects with the given altitudes */
	private static Airspace generateAirspace(float... altitudes)
	{
		Airspace airspace = generateAirspace(altitudes.length);

		// Set their positions
		int i = 0;
		for (AirspaceObject object : airspace.getActiveObjects())
		{
			object.altitude = altitudes[i];
			object.setTargetAltitude(altitudes[i]);
			i++;
		}

		return airspace;
	}

	/** Fake {@link AirspaceObject} class used for testing */
	private static class AirspaceObjectMock extends AirspaceObject
	{
		public boolean finished;
		public FlightPlan flightPlan;

		@Override public void draw(Object state) { }
		@Override public float getSize() { return 64; }
		@Override public float getAscentRate() { return 100; }
		@Override public float getMinSpeed() { return 0; }
		@Override public float getMaxSpeed() { return 100; }
		@Override public float getMinAltitude() { return 0; }
		@Override public float getMaxAltitude() { return 5000; }
		@Override public float getMaxAcceleration() { return 100; }
		@Override public float getMaxTurnRate() { return 1; }

		@Override public boolean isFinished() { return finished; }
		@Override public FlightPlan getFlightPlan() { return flightPlan; }
	}

	/**
	 * Fake {@link FlightPlanGenerator} class used for testing
	 *
	 * <p>This class only creates objects for takeOffs (no "random" objects)
	 */
	private static class FlightPathGeneratorMock extends FlightPlanGenerator
	{
		@Override
		public FlightPlan makeFlightPlan(Airspace airspace, float delta)
		{
			return null;
		}

		@Override
		public FlightPlan makeFlightPlanNow(Airspace airspace, boolean canLand, boolean isOnRunway)
		{
			return makeFakeFlightPlan(canLand);
		}
	}

	/** Implementation of {@link AirspaceObjectFactory} used for testing */
	private static class AirspaceObjectFactoryImpl implements AirspaceObjectFactory
	{
		@Override
		public AirspaceObject makeObject(Airspace airspace, FlightPlan flightPlan)
		{
			return new AirspaceObjectMock();
		}
	}
}
