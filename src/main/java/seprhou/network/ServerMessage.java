package seprhou.network;

/**
 * Interface for messages which are sent by the server and received by the client
 */
interface ServerMessage
{
	/**
	 * This message was received from the server
	 *
	 * @param client client object which received the message
	 */
	void receivedFromServer(MultiClient client);
}
