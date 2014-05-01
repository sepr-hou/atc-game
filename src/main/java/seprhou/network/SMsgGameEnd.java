package seprhou.network;

/**
 * End game message
 *
 * <p>Sent by server when the game ends (collision)
 */
class SMsgGameEnd implements ServerMessage
{
	/** Create a new game end message */
	public SMsgGameEnd()
	{
	}

	@Override
	public void receivedFromServer(MultiClient client)
	{
		if (client.isConnected())
			client.serverGameOver = true;
	}
}
