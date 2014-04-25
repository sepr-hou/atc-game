package seprhou.network.message;

import seprhou.network.MultiClient;

/**
 * Aircraft destroyed message
 *
 * <p>Sent by server when an aircraft leaves the game (out of bounds, landed)
 */
public class SMsgAircraftDestroy implements ServerMessage
{
	private int id;

	/**
	 * Create a new aircraft destroy message
	 *
	 * @param id id of destroyed aircraft
	 */
	public SMsgAircraftDestroy(int id)
	{
		this.id = id;
	}

	/** Private constructor for Kryo */
	private SMsgAircraftDestroy()
	{
	}

	@Override
	public void receivedFromServer(MultiClient client)
	{

	}
}
