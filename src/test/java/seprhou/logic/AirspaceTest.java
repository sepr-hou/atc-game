package seprhou.logic;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Airspace}
 */
@RunWith(JUnit4.class)
public class AirspaceTest {
	private static final Rectangle DIMENSIONS = new Rectangle(1000, 1000);
	private static final float SEPARATION = 200;

	@Test
	public void testObjectCulling() {
		Vector2D objectOk = new Vector2D(100, 100);
		Vector2D objectCulled = new Vector2D(2000, 100);
		Airspace airspace = AirspaceTest.generateAirspace(objectOk, objectCulled);

		// Validate state before refresh
		Assert.assertThat(airspace.getCulledObjects(), Matchers.hasSize(0));
		Assert.assertThat(airspace.getActiveObjects(), Matchers.hasSize(2));

		// After one iteration
		airspace.refresh(1);
		Assert.assertThat(airspace.getCulledObjects(), Matchers.hasSize(1));
		Assert.assertThat(airspace.getActiveObjects(), Matchers.hasSize(1));

		// Culled object is purged after another iteration
		airspace.refresh(1);
		Assert.assertThat(airspace.getCulledObjects(), Matchers.hasSize(0));
		Assert.assertThat(airspace.getActiveObjects(), Matchers.hasSize(1));
	}

	@Test
	public void testActiveObjectsOrder() {
		Airspace airspace = AirspaceTest.generateAirspace(1, 4, 8, 0, 4);
		airspace.refresh(1);

		// Ensure objects are in order
		Collection<AirspaceObject> activeObjects = airspace.getActiveObjects();
		Assert.assertThat(activeObjects, Matchers.hasSize(5));

		List<Float> altitudes = new ArrayList<>();
		for (AirspaceObject object : activeObjects) {
			altitudes.add(object.getAltitude());
		}

		Assert.assertThat(altitudes, Matchers.contains(0f, 1f, 4f, 4f, 8f));
	}

	@Test
	public void testCollisionWarningsAltitude() {
		Airspace airspace = AirspaceTest.generateAirspace(100, 400, 800, 0, 400);
		airspace.refresh(1);

		/*
		 * Warnings between: 0 - 100 400 - 400
		 */

		// Test against actual collision warnings
		Collection<CollisionWarning> warnings = airspace.getCollisionWarnings();
		Assert.assertThat(warnings, Matchers.hasSize(2));

		for (CollisionWarning warning : warnings) {
			// Check which warning it is
			if (warning.getObject1().getAltitude() == 400) {
				Assert.assertThat(warning.getObject2().getAltitude(), Matchers.is(400f));
			} else if (warning.getObject1().getAltitude() == 100) {
				Assert.assertThat(warning.getObject2().getAltitude(), Matchers.is(0f));
			} else {
				Assert.assertThat(warning.getObject1().getAltitude(), Matchers.is(0f));
				Assert.assertThat(warning.getObject2().getAltitude(), Matchers.is(100f));
			}
		}
	}

	@Test
	public void testGameOver() {
		Airspace airspace1 = AirspaceTest.generateAirspace(1, 5000);
		Airspace airspace2 = AirspaceTest.generateAirspace(1, 2);

		// Airspace 1 is bad, airspace 2 is good
		airspace1.refresh(1);
		airspace2.refresh(1);
		Assert.assertThat(airspace1.isGameOver(), Matchers.is(false));
		Assert.assertThat(airspace2.isGameOver(), Matchers.is(true));
	}

	@Test
	public void testFindAircraft() {
		Vector2D obj1 = new Vector2D(100, 100);
		Vector2D obj2 = new Vector2D(200, 200);
		Vector2D obj3 = new Vector2D(300, 300);
		Airspace airspace = AirspaceTest.generateAirspace(obj1, obj2, obj3);
		airspace.refresh(1);

		// Test find aircraft clicking
		AirspaceObject found = airspace.findAircraft(obj2);
		Assert.assertThat(found, Matchers.is(Matchers.notNullValue()));
		Assert.assertThat(found.getPosition(), Matchers.is(obj2));

		// Test none in range
		AirspaceObject found2 = airspace.findAircraft(new Vector2D(5000, 400));
		Assert.assertThat(found2, Matchers.is(Matchers.nullValue()));

		// Test circle
		List<Vector2D> positions = AirspaceTest.objectsToPositions(airspace.findAircraft(obj3, 200));
		Assert.assertThat(positions, Matchers.containsInAnyOrder(obj2, obj3));
	}

	@Test
	public void testFindAircraftAltitude() {
		Airspace airspace = AirspaceTest.generateAirspace(1, 2, 3);
		airspace.refresh(1);

		// Aircraft search should return 3
		AirspaceObject found = airspace.findAircraft(Vector2D.ZERO);
		Assert.assertThat(found, Matchers.is(Matchers.notNullValue()));
		Assert.assertThat(found.getAltitude(), Matchers.is(3f));
	}

	/** Converts a list of objects to a list of positions */
	private static List<Vector2D> objectsToPositions(Iterable<AirspaceObject> objects) {
		List<Vector2D> positions = new ArrayList<>();

		for (AirspaceObject object : objects) {
			positions.add(object.getPosition());
		}

		return positions;
	}

	/** Generates an airspace with the given number of objects */
	private static Airspace generateAirspace(final int objectCount) {
		// Configure airspace
		Airspace airspace = new Airspace();
		airspace.setDimensions(AirspaceTest.DIMENSIONS);
		airspace.setLateralSeparation(AirspaceTest.SEPARATION);
		airspace.setVerticalSeparation(AirspaceTest.SEPARATION);
		airspace.setObjectFactory(new AirspaceObjectFactory() {
			private int objectsLeft = objectCount;

			@Override
			public AirspaceObject makeObject(Airspace airspace, float delta) {
				if (this.objectsLeft > 0) {
					this.objectsLeft--;
					return new AirspaceObjectMock();
				}

				return null;
			}
		});

		// Generate some objects
		for (int i = 0; i < objectCount; i++) {
			airspace.refresh(0);
		}

		// Solidify them
		for (AirspaceObject object : airspace.getActiveObjects()) {
			((AirspaceObjectMock) object).solid = true;
		}

		// The game should not be over
		Assert.assertThat(airspace.isGameOver(), Matchers.is(false));
		return airspace;
	}

	/** Generates an airspace with some objects with the given positions */
	private static Airspace generateAirspace(Vector2D... objects) {
		Airspace airspace = AirspaceTest.generateAirspace(objects.length);

		// Set their positions
		int i = 0;
		for (AirspaceObject object : airspace.getActiveObjects()) {
			object.position = objects[i];
			i++;
		}

		return airspace;
	}

	/** Generates an airspace with some objects with the given altitudes */
	private static Airspace generateAirspace(float... altitudes) {
		Airspace airspace = AirspaceTest.generateAirspace(altitudes.length);

		// Set their positions
		int i = 0;
		for (AirspaceObject object : airspace.getActiveObjects()) {
			object.altitude = altitudes[i];
			object.setTargetAltitude(altitudes[i]);
			i++;
		}

		return airspace;
	}

	/** Fake {@link AirspaceObject} class used for testing */
	private static class AirspaceObjectMock extends AirspaceObject {
		public boolean solid;

		@Override
		public boolean isSolid() {
			return this.solid;
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

		@Override
		public void setViolated(boolean value) {}
	}
}
