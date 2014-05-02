package seprhou.network;

/**
 * Start game message
 *
 * <p>Sent by server to begin a new game (after it has ended)
 */
class SMsgGameStart implements ServerMessage
{
	private float lateral, vertical;

	/**
	 * Create a new game start message
	 *
	 * @param lateral lateral separation distance (passed to new airspace)
	 * @param vertical vertical separation distance (passed to new airspace)
	 */
	public SMsgGameStart(float lateral, float vertical)
	{
		this.lateral = lateral;
		this.vertical = vertical;
	}

	/** Private constructor for Kryo */
	@SuppressWarnings("unused")
	private SMsgGameStart()
	{
	}

	@Override
	public void receivedFromServer(MultiClient client)
	{
		client.startGame(lateral, vertical);
	}
}
