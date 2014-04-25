package seprhou.network;

import java.io.Closeable;
import java.io.IOException;

/**
 * An endpoint of the network
 *
 * <p>This interface allows the GUI code to interface with the network code without worrying
 * about if we're the server, client or on a single player game.
 */
public interface NetworkEndpoint extends Closeable
{
	/**
	 * Method called at the start of the global act method
	 *
	 * <p>You should call this method once per frame, before acting on anything in the airspace.
	 * <p>This method will receive + process messages from the other endpoint
	 */
	void actBegin()  throws IOException;

	/**
	 * Method called at the end of the global act method
	 *
	 * <p>You should call this method once per frame, after acting on anything in the airspace.
	 * <p>This method will refresh the airspace and send updates / requests to the other endpoint
	 *
	 * @param delta time since last frame (seconds)
	 */
	void actEnd(float delta) throws IOException;
}
