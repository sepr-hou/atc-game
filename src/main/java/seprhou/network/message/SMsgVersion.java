package seprhou.network.message;

import seprhou.network.MPClient;
import seprhou.network.NetworkCommon;

/**
 * Version message
 *
 * <p>Sent by server to verify the protocol version
 */
public class SMsgVersion implements ServerMessage
{
	private int version;

	/** Creates a new version message using my protocol version */
	public SMsgVersion()
	{
		this.version = NetworkCommon.PROTOCOL_VERSION;
	}

	@Override
	public void receivedFromServer(MPClient client)
	{
		//
	}
}
