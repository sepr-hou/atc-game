package seprhou.network;

import com.esotericsoftware.minlog.Log;

/**
 * Version message
 *
 * <p>Sent by server to verify the protocol version
 */
class SMsgVersion implements ServerMessage
{
	private int version;

	/** Creates a new version message using my protocol version */
	public SMsgVersion()
	{
		this.version = NetworkCommon.PROTOCOL_VERSION;
	}

	@Override
	public void receivedFromServer(MultiClient client)
	{
		// Verify version
		if (version != NetworkCommon.PROTOCOL_VERSION)
		{
			String logMsg;

			if (version > NetworkCommon.PROTOCOL_VERSION)
			{
				logMsg = "[Client] Version of the server is too new for me to understand " +
						"(" + version + " > " + NetworkCommon.PROTOCOL_VERSION + "). Please update!";
			}
			else
			{
				logMsg = "[Client] Version of the server is old " +
						"(" + version + " < " + NetworkCommon.PROTOCOL_VERSION + "). " +
						"Please tell the other player to update!";
			}

			Log.error(logMsg);
			client.close();
		}
	}
}
