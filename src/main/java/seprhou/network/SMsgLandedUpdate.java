package seprhou.network;

/**
 * Update the number of landed planes
 *
 * <p>Sent by server when the number of landed planes changes
 */
class SMsgLandedUpdate implements ServerMessage
{
	private int landed;

	/**
	 * Create a new landed objects update message
	 *
	 * @param landed number of landed objects
	 */
	public SMsgLandedUpdate(int landed)
	{
		this.landed = landed;
	}

	/** Private constructor for Kryo */
	@SuppressWarnings("unused")
	private SMsgLandedUpdate()
	{
	}

	@Override
	public void receivedFromServer(MultiClient client)
	{
		if (client.isConnected())
		{
			client.getAirspace().setLandedObjects(landed);
		}
	}
}
