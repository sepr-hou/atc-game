package seprhou.network;

/**
 * Ask the server to set the speed of an aircraft
 *
 * <p>Invalid messages received (invalid aircraft id, target out of bounds) are ignored.
 */
class CMsgSetSpeed implements ClientMessage
{
	private int aircraftId;
	private float newTarget;

	/**
	 * Creates a new set speed message
	 *
	 * @param aircraftId aircraft to change speed of
	 * @param newTarget new target speed
	 */
	public CMsgSetSpeed(int aircraftId, float newTarget)
	{
		this.aircraftId = aircraftId;
		this.newTarget = newTarget;
	}

	/** Private constructor for Kryo */
	@SuppressWarnings("unused")
	private CMsgSetSpeed()
	{
	}

	@Override
	public void receivedFromClient(MultiServer server)
	{

	}
}
