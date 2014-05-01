package seprhou.network;

/**
 * Update the score
 *
 * <p>Sent by server when the score changes
 */
class SMsgScoreUpdate implements ServerMessage
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
	@SuppressWarnings("unused")
	private SMsgScoreUpdate()
	{
	}

	@Override
	public void receivedFromServer(MultiClient client)
	{
		if (client.isConnected())
		{
			client.getAirspace().setScore(score);
		}
	}
}
