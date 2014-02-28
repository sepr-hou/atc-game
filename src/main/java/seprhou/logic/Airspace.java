package seprhou.logic;

import java.util.*;
import seprhou.gui.GameScreen;

/**
 * Controls an entire air space and all the aircraft in it
 *
 * <p>
 * The game logic is designed to be polled each frame for events rather than
 * call callback methods. Each frame, you should call the {@link #refresh(float)}
 * method, and then use the other methods to observe the new state of the game.
 */
public class Airspace
{
	private AirspaceObjectFactory objectFactory;
	private Rectangle dimensions;
	private float lateralSeparation, verticalSeparation;
	private int cycleCount = 0;

	private final ArrayList<AirspaceObject> culledObjects = new ArrayList<>();
	private final ArrayList<CollisionWarning> collisionWarnings = new ArrayList<>();
	private final Queue<AirspaceObject> landedObjects = new LinkedList<AirspaceObject>();

	/** During the game refresh, this list is sorted so that LOWER planes (altitude) are put FIRST */
	private final ArrayList<AirspaceObject> activeObjects = new ArrayList<>();

	private boolean gameOver;
	private int score = 0;
	// Needed to prevent more planes than allowed from landing.
	private int landingPlanes = 0;

	/** Returns the factory responsible for constructing airspace objects */
	public AirspaceObjectFactory getObjectFactory()
	{
		return objectFactory;
	}

	/**
	 * Sets the class used by this airspace for creating aircraft
	 *
	 * @param factory new object factory (not null)
	 */
	public void setObjectFactory(AirspaceObjectFactory factory)
	{
		if (factory == null)
			throw new IllegalArgumentException("factory cannot be null");

		this.objectFactory = factory;
	}

	/** Returns the dimensions of the airspace */
	public Rectangle getDimensions()
	{
		return dimensions;
	}

	/**
	 * Sets the dimensions of the game area to the given rectangle
	 *
	 * @param dimensions new airspace dimensions
	 */
	public void setDimensions(Rectangle dimensions)
	{
		if (dimensions == null)
			throw new IllegalArgumentException("dimensions cannot be null");

		this.dimensions = dimensions;
	}

	/**
	 * Returns the lateral separation distance to generate warnings at
	 *
	 * <p>This only affects the collision warnings. Actual crashes are determined from the aircraft sizes
	 *
	 * @see AirspaceObject#getSize()
	 */
	public float getLateralSeparation()
	{
		return lateralSeparation;
	}

	/**
	 * Sets the new lateral separation distance
	 *
	 * @param separation lateral separation distance to generate warnings at
	 * @see #getLateralSeparation()
	 */
	public void setLateralSeparation(float separation)
	{
		if (separation <= 0)
			throw new IllegalArgumentException("separation must be greater than 0");

		this.lateralSeparation = separation;
	}

	/**
	 * Returns the vertical separation distance to generate warnings at
	 *
	 * <p>This only affects the collision warnings. Actual crashes are determined from the aircraft sizes
	 *
	 * @see AirspaceObject#getSize()
	 */
	public float getVerticalSeparation()
	{
		return verticalSeparation;
	}

	/**
	 * Sets the new vertical separation distance
	 *
	 * @param separation vertical separation distance to generate warnings at
	 * @see #getVerticalSeparation()
	 */
	public void setVerticalSeparation(float separation)
	{
		if (separation <= 0)
			throw new IllegalArgumentException("separation must be greater than 0");

		this.verticalSeparation = separation;
	}

	/**
	 * Throws an exception if the class has not been initialized properly
	 */
	private void ensureInitialized()
	{
		if (objectFactory == null || dimensions == null ||
				lateralSeparation == 0 || verticalSeparation == 0)
		{
			throw new IllegalStateException("The Airspace class must be initialized before use");
		}
	}

	/**
	 * Returns the aircraft which were culled during the last refresh
	 *
	 * <p>
	 * This is the list of aircraft which flew off the screen and
	 * were therefore culled / destroyed.
	 *
	 * <p>
	 * The returned list is unmodifiable and is reused each refresh (so you must
	 * copy it if you want to keep it between refreshes)
	 */
	public Collection<AirspaceObject> getCulledObjects()
	{
		return Collections.unmodifiableCollection(culledObjects);
	}

	/**
	 * Returns the list of Objects "landed" in the game area List works on a
	 * FIFO basis, Objects take off in the order they were landed and not in any
	 * other way
	 */
	public Queue<AirspaceObject> getLandedObjects() {
		return this.landedObjects;
	}

	public void takeOff() {
		AirspaceObject planeTakingOff = this.getLandedObjects().poll();
		if (planeTakingOff != null) {
			FlightPlan newFlightPlan = GameScreen.flightPathGenerator
					.makeFlightPlanNow(this, false, true);
			planeTakingOff.setFlightPlan(newFlightPlan);
			planeTakingOff.resetRunwayPlane();
			this.getActiveObjects().add(planeTakingOff);
		}
	}

	/**
	 * Returns the list of active aircraft
	 *
	 * <p>These objects are sorted by altitude (small altitudes first)
	 */
	public Collection<AirspaceObject> getActiveObjects()
	{
		return activeObjects;
	}

	/**
	 * Returns the list of collision warnings generated during the last refresh
	 *
	 * <p>
	 * The returned list is unmodifiable and is reused each refresh (so you must
	 * copy it if you want to keep it between refreshes)
	 */
	public Collection<CollisionWarning> getCollisionWarnings()
	{
		return Collections.unmodifiableCollection(collisionWarnings);
	}

	/**
	 * Finds the aircraft which is occupying the given point
	 *
	 * <p>
	 * This method takes into account the size of aircraft to determine where they are.
	 * If multiple aircraft occupy a point, the one with the HIGHEST altitude will be chosen
	 *
	 * @param centre point to start search at
	 * @return the aircraft found occupying the given point
	 */
	public AirspaceObject findAircraft(Vector2D centre)
	{
		// Iterate through all objects IN REVERSE and return the first found
		for (int i = activeObjects.size() - 1; i >= 0; i--)
		{
			AirspaceObject current = activeObjects.get(i);
			float distance = current.getPosition().distanceTo(centre);

			if (distance < current.getSize())
				return current;
		}

		return null;
	}

	// Iterates a pointer through all Objects, returning the value of whichever
	// object is currently beign pointed at
	public AirspaceObject cycleAircraft() {
		if (this.activeObjects.size() == 0)
			return null;
		cycleCount++;
		cycleCount %= this.activeObjects.size();
		return this.activeObjects.get(cycleCount);
	}

	/**
	 * Finds the aircraft who's centres are within the given circle
	 *
	 * <p>
	 * Objects are ordered with highest altitude first
	 *
	 * @param centre the centre of the circle to search
	 * @param radius the radius of the circle to search
	 * @return the list of aircraft found - may be empty of no aircraft were found
	 */
	public List<AirspaceObject> findAircraft(Vector2D centre, float radius)
	{
		ArrayList<AirspaceObject> results = new ArrayList<>();

		// Iterate through all objects IN REVERSE and return the accepted objects
		for (int i = activeObjects.size() - 1; i >= 0; i--)
		{
			AirspaceObject current = activeObjects.get(i);
			float distance = current.getPosition().distanceTo(centre);

			if (distance < radius)
				results.add(current);
		}

		return results;
	}

	/** Returns true if the game is over */
	public boolean isGameOver()
	{
		return gameOver;
	}

	/** Culls objects outside the game area */
	private void cullObjects()
	{
		Rectangle gameArea = getDimensions();

		culledObjects.clear();

		// Test if every object is within the area and cull if not
		//  Do in reverse in case multiple objects are culled
		for (int i = activeObjects.size() - 1; i >= 0; i--)
		{
			AirspaceObject object = activeObjects.get(i);

			if (!gameArea.intersects(object.getPosition(), object.getSize()))
			{
				activeObjects.remove(i);
				culledObjects.add(object);
			}
			else
			{
				// If plane has finished flight plan
				// Add its score to score,
				// Then delete.
				if (object.isFinished()) {
					// If a plane is landing, add it to the airport
					// Add its score to score.
					if (object.getFlightPlan().isLanding()) {
						this.score += object.getScore();
						this.activeObjects.remove(i);
						this.landedObjects.add(object);
						this.landingPlanes--;
					} else {
						this.score += object.getScore();
						this.activeObjects.remove(i);
						this.culledObjects.add(object);
					}
				}
			}
		}
	}

	/** Generates the list of collision warnings */
	private void calculateCollisions()
	{
		// This is a simple collision algorithm, going through all objects
		// and checking them with all other objects.
		// As such it is O(n^2) so it may be slow for lots of objects

		float lateralSeparation = getLateralSeparation();
		float vertSeparation = getVerticalSeparation();
		int objectsCount = activeObjects.size();

		// Erase existing warnings
		collisionWarnings.clear();

		// Find new warnings
		for (int a = 0; a < objectsCount; a++)
		{
			AirspaceObject object1 = activeObjects.get(a);

			// Ignore non-solid objects
			if (!object1.isSolid())
				continue;

			Vector2D object1Position = object1.getPosition();
			float object1Altitude = object1.getAltitude();

			for (int b = a + 1; b < objectsCount; b++)
			{
				AirspaceObject object2 = activeObjects.get(b);

				// Ignore non-solid objects
				if (!object2.isSolid())
					continue;

				// Test collision
				if (object1Position.distanceTo(object2.getPosition()) < lateralSeparation &&
					Math.abs(object1Altitude - object2.getAltitude()) < vertSeparation)
				{
					// Add collision warning
					object1.setViolated(true);
					object2.setViolated(true);
					
					CollisionWarning warning = new CollisionWarning(object1, object2);
					collisionWarnings.add(warning);

					if (warning.hasCollided())
						gameOver = true;
				}
			}
		}
	}

	/**
	 * Called every so often to refresh the game state
	 *
	 * <p>
	 * Normally this is called every frame before the game is drawn to the screen.
	 *
	 * <p>
	 * The {@code delta} parameter allows the game to be frame-rate independent.
	 * This means the game should appear to run at the same speed regardless of the frame-rate.
	 * If you do not want this, you can use the value {@code 1 / fps} as the value for {@code delta}
	 *
	 * @param delta the time (in seconds) since the last refresh
	 */
	public void refresh(float delta)
	{
		ensureInitialized();

		// Refresh all active objects
		for (AirspaceObject current : activeObjects)
			current.refresh(delta);

		// Cull any objects outside the game area
		cullObjects();

		// Add new aircraft
		AirspaceObject newObject = getObjectFactory().makeObject(this, delta);
		if (newObject != null)
			activeObjects.add(newObject);

		// Generate collision warnings + determine if game is over
		calculateCollisions();

		// Sort the list of aircraft
		Collections.sort(activeObjects, AltitudeComparator.INSTANCE);
	}

	/**
	 * Draws all objects in the airspace
	 *
	 * <p>
	 * This effectively calls {@link AirspaceObject#draw(Object)} on all objects.
	 *
	 * @param state any state information to be passed to the drawer
	 */
	public void draw(Object state)
	{
		// Draw forwards to draw lower planes first
		for (AirspaceObject current : activeObjects)
			current.draw(state);
	}

	/** Comparator comparing by altitude */
	private static class AltitudeComparator implements Comparator<AirspaceObject>
	{
		public static final AltitudeComparator INSTANCE = new AltitudeComparator();

		@Override
		public int compare(AirspaceObject o1, AirspaceObject o2)
		{
			return Float.compare(o1.getAltitude(), o2.getAltitude());
		}
	}

	/** Returns the current Score */
	public int getScore() {
		return this.score;
	}

	/** Sets the Score to an integer provided */
	public void setScore(int score) {
		this.score = score;
	}

	/** Returns the list of all Planes landed in the Airspace */
	public int getLandingPlanes() {
		return landingPlanes;
	}

	/** Sets the List of all Planes that have landed in the Airspace */
	public void setLandingPlanes(int landingPlanes) {
		this.landingPlanes = landingPlanes;
	}

}
