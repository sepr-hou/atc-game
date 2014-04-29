package seprhou.network.message;

import seprhou.network.MultiServer;

/**
 * Ask the server to make a plane takeoff
 */
public class CMsgTakeoff implements ClientMessage
{
	/** Creates a new takeoff message */
	public CMsgTakeoff()
	{
	}

	@Override
	public void receivedFromClient(MultiServer server)
	{

	}
}
