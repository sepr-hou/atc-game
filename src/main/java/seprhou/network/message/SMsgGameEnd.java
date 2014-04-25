package seprhou.network.message;

import seprhou.network.MPClient;

/**
 * End game message
 *
 * <p>Sent by server when the game ends (collision)
 */
public class SMsgGameEnd implements ServerMessage
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
	public void receivedFromServer(MPClient client)
	{
		
	}
}
