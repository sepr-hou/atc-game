package seprhou.network.message;

/**
 * Start game message
 *
 * <p>Sent by server to begin a new game (after it has ended)
 */
public class SMsgGameStart implements ServerMessage
{
	private float verticalSep, lateralSep;

	/**
	 * Create a new game start message
	 *
	 * @param verticalSep vertical separation distance (passed to new airspace)
	 * @param lateralSep lateral separation distance (passed to new airspace)
	 */
	public SMsgGameStart(float verticalSep, float lateralSep)
	{
		this.verticalSep = verticalSep;
		this.lateralSep = lateralSep;
	}

	/** Private constructor for Kryo */
	private SMsgGameStart()
	{
	}

	@Override
	public void receivedFromServer()
	{

	}
}
