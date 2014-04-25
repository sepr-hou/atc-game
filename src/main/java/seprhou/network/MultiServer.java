package seprhou.network;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import seprhou.logic.Airspace;

import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import seprhou.network.message.ClientMessage;

/**
 * Class which implements the host side of the multiplayer game
 */
public class MultiServer implements NetworkEndpoint
{
	private final Server server = new Server();
	private final Queue<ClientMessage> messageQueue = new LinkedList<>();

	private Connection otherEndpoint;
	private Airspace airspace;

	/**
	 * Creates and starts the server
	 *
	 * @throws IOException thrown if an IO error occurs
	 */
	public MultiServer() throws IOException
	{
		NetworkCommon.register(server.getKryo());
		server.addListener(new MyListener());
		server.bind(NetworkCommon.PORT);
	}

	@Override
	public boolean isConnected()
	{
		return otherEndpoint != null;
	}

	@Override
	public Airspace getAirspace()
	{
		return airspace;
	}

	@Override
	public void actBegin() throws IOException
	{
		// Update server
		server.update(0);

		// Process any messages in the queue
		for (;;)
		{
			ClientMessage msg = messageQueue.poll();
			if (msg == null)
				break;

			msg.receivedFromClient(this);
		}
	}

	@Override
	public void actEnd(float delta) throws IOException
	{
		// Ignore if not connected
		if (!isConnected())
			return;

		// TODO send updates

		// Update server
		server.update(0);
	}

	@Override
	public void close() throws IOException
	{
		server.close();
	}

	/** Listener for the server (single threaded) */
	private class MyListener extends Listener
	{
		public void connected(Connection other)
		{
			// Reject multiple clients
			if (isConnected())
			{
				other.close();
			}
			else
			{
				Log.info("[Server] Client " + other.getRemoteAddressTCP() + " has connected");
				otherEndpoint = other;

				// TODO handle this
			}
		}

		public void disconnected(Connection other)
		{
			if (other == otherEndpoint)
			{
				Log.info("[Server] Client has disconnected");
				otherEndpoint = null;

				// TODO handle this
			}

			other.close();
		}

		public void received(Connection other, Object obj)
		{
			// Add to message queue
			if (obj instanceof ClientMessage)
				messageQueue.add((ClientMessage) obj);
		}
	}
}
