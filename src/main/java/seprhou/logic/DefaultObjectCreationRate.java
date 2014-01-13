package seprhou.logic;

import java.util.Random;

/**
 * Class which randomly produces boolean values defining when objects are created
 */
public class DefaultObjectCreationRate
{
	private static final float RATE_PER_SEC = 0.2f;
	private static final float MIN_TIME = 1f;

	private final Random random = new Random();
	private float timeSinceLastAircraft;

	/**
	 * Returns true if a new object should be created this refresh
	 *
	 * @param airspace airspace to create within
	 * @param delta time since last refresh (secs)
	 */
	public boolean nextBoolean(Airspace airspace, float delta)
	{
		timeSinceLastAircraft += delta;

		// Check Minimum time between aircraft
		if (timeSinceLastAircraft < MIN_TIME)
			return false;

		// Add some randomness
		if (random.nextFloat() < delta * RATE_PER_SEC)
		{
			timeSinceLastAircraft = 0;
			return true;
		}

		return false;
	}
}
