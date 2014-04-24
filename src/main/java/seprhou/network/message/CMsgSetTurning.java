package seprhou.network.message;

/**
 * Ask the server to begin / end turning of an aircraft
 *
 * <p>Invalid messages received (invalid aircraft id) are ignored.
 */
public class CMsgSetTurning implements ClientMessage
{
	private int aircraftId;
	private TurningState state;

	/**
	 * Creates a new set turning message
	 *
	 * @param aircraftId aircraft to change speed of
	 * @param state new turning state
	 */
	public CMsgSetTurning(int aircraftId, TurningState state)
	{
		this.aircraftId = aircraftId;
		this.state = state;
	}

	/** Private constructor for Kryo */
	private CMsgSetTurning()
	{
	}

	@Override
	public void receivedFromClient()
	{

	}
}
