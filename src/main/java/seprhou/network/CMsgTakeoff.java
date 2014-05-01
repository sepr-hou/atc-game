package seprhou.network;

/**
 * Ask the server to make a plane takeoff
 */
class CMsgTakeoff implements ClientMessage
{
	/** Creates a new takeoff message */
	public CMsgTakeoff()
	{
	}

	@Override
	public void receivedFromClient(MultiServer server)
	{
		if (server.isConnected())
		{
			server.getAirspace().takeOff();
		}
	}
}
