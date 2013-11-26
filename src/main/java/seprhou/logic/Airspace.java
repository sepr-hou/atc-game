package seprhou.logic;

import java.util.Collection;

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
	private Collection<AirspaceObject> culledObjects;
	private Collection<AirspaceObject> activeObjects;
	private Collection<CollisionWarning> collisionWarnings;

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
	 * The returned list is immutable.
	 */
	public Collection<AirspaceObject> getCulledObjects()
	{
		return culledObjects;
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
	 * The returned list is immutable.
	 */
	public Collection<CollisionWarning> getCollisionWarnings()
	{
		return collisionWarnings;
	}

	/**
	 * Finds the aircraft closest to the given point
	 *
	 * @param point point to start search at
	 * @return the closest aircraft or {@code null} if there are no active aircraft
	 */
	public AirspaceObject findAircraft(Vector2D point)
	{
		// TODO implement this
		return null;
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
		// TODO implement this
		return null;
	}

	/** Returns true if the game is over */
	public boolean isGameOver()
	{
		// TODO implement this
		return false;
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
		// TODO implement this
	}
}
