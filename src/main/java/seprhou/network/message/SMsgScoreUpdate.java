package seprhou.network.message;

/**
 * Update the score
 *
 * <p>Sent by server when the score changes
 */
public class SMsgScoreUpdate implements ServerMessage
{
	private int score;

	/**
	 * Create a new score update message
	 *
	 * @param score game score
	 */
	public SMsgScoreUpdate(int score)
	{
		this.score = score;
	}

	/** Private constructor for Kryo */
	private SMsgScoreUpdate()
	{
	}

	@Override
	public void receivedFromServer()
	{

	}
}
