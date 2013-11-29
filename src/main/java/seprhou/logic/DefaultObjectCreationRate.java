package seprhou.logic;

import java.util.Random;

/**
 * Class which randomly produces boolean values defining when objects are created
 */
public class DefaultObjectCreationRate
{
	private final Random random = new Random();

	/**
	 * Returns true if a new object should be created this refresh
	 *
	 * @param airspace airspace to create within
	 * @param delta time since last refresh (secs)
	 */
	public boolean nextBoolean(Airspace airspace, float delta)
	{
		// TODO Implement this
		return false;
	}
}
