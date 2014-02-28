package seprhou.logic;

/**
 * Interface which is responsible for creating all the new aircraft in the game
 * <p>
 * By implementing this class, you can control when all aircraft are generated,
 * what the aircraft are, and what their flight plans are.
 */
public interface AirspaceObjectFactory
{
	/**
	 * Creates a new airspace object
	 *
	 * <p>
	 * This method is called every game refresh to create a game aircraft.
	 * Most of the time, this method would return {@code null} - indicating that
	 * no aircraft are to be introduced this turn.
	 *
	 * @param airspace the airspace the object will be created in
	 * @param delta time (in seconds) since last game refresh
	 * @return the new object or {@code null} to create no object this refresh
	 */
	public AirspaceObject makeObject(Airspace airspace, float delta);
}
