package seprhou.network.message;

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
		this.version = MessageSetup.PROTOCOL_VERSION;
	}

	@Override
	public void receivedFromServer()
	{
		//
	}
}
