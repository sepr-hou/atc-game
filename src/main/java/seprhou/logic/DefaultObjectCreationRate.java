package seprhou.logic;

import java.util.Random;

/**
 * Class which randomly produces boolean values defining when objects are created
 */
public class DefaultObjectCreationRate
{
	private static float RATE_PER_SEC = 0.2f;

	private final Random random = new Random();

	/**
	 * Returns true if a new object should be created this refresh
	 *
	 * @param airspace airspace to create within
	 * @param delta time since last refresh (secs)
	 */
	public boolean nextBoolean(Airspace airspace, float delta)
	{
		// TODO This might be "too simple" and need changing later
		return random.nextFloat() < delta * RATE_PER_SEC;
	}
}
