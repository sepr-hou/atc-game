package seprhou.network.message;

import seprhou.network.MultiServer;

/**
 * Interface for messages which are sent by the client and received by the server
 */
public interface ClientMessage
{
	/**
	 * This message was received from the client
	 *
	 * @param server server object which received the message
	 */
	void receivedFromClient(MultiServer server);
}
