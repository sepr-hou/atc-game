package seprhou.network;

/**
 * Ask the server to set the altitude of an aircraft
 *
 * <p>Invalid messages received (invalid aircraft id, target out of bounds) are ignored.
 */
class CMsgSetAltitude implements ClientMessage
{
	private int aircraftId;
	private float newTarget;

	/**
	 * Creates a new set altitude message
	 *
	 * @param aircraftId aircraft to change altitude of
	 * @param newTarget new target altitude
	 */
	public CMsgSetAltitude(int aircraftId, float newTarget)
	{
		this.aircraftId = aircraftId;
		this.newTarget = newTarget;
	}

	/** Private constructor for Kryo */
	private CMsgSetAltitude()
	{
	}

	@Override
	public void receivedFromClient(MultiServer server)
	{

	}
}
