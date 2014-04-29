package seprhou.network;

/**
 * Interface for messages which are sent by the client and received by the server
 */
interface ClientMessage
{
	/**
	 * This message was received from the client
	 *
	 * @param server server object which received the message
	 */
	void receivedFromClient(MultiServer server);
}
