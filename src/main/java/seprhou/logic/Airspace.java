package seprhou.logic;

import java.util.*;

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
	private final AirspaceConfig config;
	private ArrayList<AirspaceObject> culledObjects = new ArrayList<>();
	private ArrayList<AirspaceObject> activeObjects = new ArrayList<>();
	private ArrayList<CollisionWarning> collisionWarnings = new ArrayList<>();

	private boolean gameOver;

	/**
	 * Constructs a new Airspace with the given configuration options
	 *
	 * @param config airspace configuration
	 */
	public Airspace(AirspaceConfig config)
	{
		this.config = config;
	}

	/** Returns the airspace configuration */
	public AirspaceConfig getConfig()
	{
		return config;
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

	/** Returns the list of active aircraft */
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
	 * Finds the aircraft closest to the given point
	 *
	 * @param point point to start search at
	 * @return the closest aircraft or {@code null} if there are no active aircraft
	 */
	public AirspaceObject findAircraft(Vector2D point)
	{
		AirspaceObject bestObject = null;
		float bestDistance = Float.POSITIVE_INFINITY;

		// Iterate through all objects and return the one closest
		for (AirspaceObject current : activeObjects)
		{
			float distance = current.getPosition().distanceTo(point);

			if (distance < bestDistance)
			{
				bestDistance = distance;
				bestObject = current;
			}
		}

		return bestObject;
	}

	/**
	 * Finds the aircraft who's centres are within the given circle
	 *
	 * @param centre the centre of the circle to search
	 * @param radius the radius of the circle to search
	 * @return the list of aircraft found - may be empty of no aircraft were found
	 */
	public Collection<AirspaceObject> findAircraft(Vector2D centre, float radius)
	{
		ArrayList<AirspaceObject> results = new ArrayList<>();

		// Iterate through all objects and return the one closest
		for (AirspaceObject current : activeObjects)
		{
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
		Rectangle gameArea = config.getDimensions();

		culledObjects.clear();

		// Test if every object is within the area and cull if not
		//  Do in reverse in case multiple objects are culled
		for (int i = activeObjects.size() - 1; i >= 0; i--)
		{
			AirspaceObject object = activeObjects.get(i);

			if (gameArea.intersects(object.getPosition(), object.getSize()))
			{
				activeObjects.remove(i);
				culledObjects.add(object);
			}
		}
	}

	/** Generates the list of collision warnings */
	private void calculateCollisions()
	{
		// This is a simple collision algorithm, going through all objects
		// and checking them with all other objects.
		// As such it is O(n^2) so it may be slow for lots of objects

		float horizSeparation = config.getHorizontalSeparation();
		float vertSeparation = config.getVerticalSeparation();
		int objectsCount = activeObjects.size();

		// Erase existing warnings
		collisionWarnings.clear();

		// Find new warnings
		for (int a = 0; a < objectsCount; a++)
		{
			AirspaceObject object1 = activeObjects.get(a);
			Vector2D object1Position = object1.getPosition();
			float object1Altitude = object1.getAltitude();

			for (int b = a + 1; b < objectsCount; b++)
			{
				AirspaceObject object2 = activeObjects.get(b);

				// Test collision
				if (object1Position.distanceTo(object2.getPosition()) < horizSeparation ||
					Math.abs(object1Altitude - object2.getAltitude()) < vertSeparation)
				{
					// Add collision warning
					CollisionWarning warning = new CollisionWarning(object1, object2);

					collisionWarnings.add(new CollisionWarning(object1, object2));

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
		// Refresh all active objects
		for (AirspaceObject current : activeObjects)
			current.refresh(delta);

		// Cull any objects outside the game area
		cullObjects();

		// Add new aircraft
		AirspaceObject newObject = config.getObjectFactory().makeObject(this, delta);
		if (newObject != null)
			activeObjects.add(newObject);

		// Generate collision warnings + determine if game is over
		calculateCollisions();
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
		for (AirspaceObject current : activeObjects)
			current.draw(state);
	}
}
