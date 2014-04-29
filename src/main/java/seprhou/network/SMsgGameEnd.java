package seprhou.network;

/**
 * End game message
 *
 * <p>Sent by server when the game ends (collision)
 */
class SMsgGameEnd implements ServerMessage
{
	private float finalTime;
	private int finalScore;

	/**
	 * Create a new game end message
	 *
	 * @param time final game time (seconds)
	 * @param score final game score
	 */
	public SMsgGameEnd(float time, int score)
	{
		this.finalTime = time;
		this.finalScore = score;
	}

	/** Private constructor for Kryo */
	private SMsgGameEnd()
	{
	}

	@Override
	public void receivedFromServer(MultiClient client)
	{

	}
}
