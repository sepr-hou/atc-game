package seprhou.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import seprhou.logic.Airspace;
import seprhou.network.message.ServerMessage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Class which implements the client side of the multiplayer game
 */
public class MPClient implements NetworkEndpoint
{
	private final Client client = new Client();
	private final Queue<ServerMessage> messageQueue = new LinkedList<>();

	private ConnectionThread connectionThread;
	private Connection otherEndpoint;
	private Airspace airspace;

	/**
	 * Creates a new client and opens the connection
	 *
	 * <p>You must still call actBegin / actEnd in the game loop while connecting
	 *
	 * @param hostname name of the host to connect to
	 */
	public MPClient(String hostname)
	{
		NetworkCommon.register(client.getKryo());
		client.addListener(new MyListener());

		// Run connection attempt in new thread (so we don't block the GUI)
		connectionThread = new ConnectionThread(hostname);
		connectionThread.start();
	}

	@Override
	public void actBegin() throws IOException
	{
		// Update client
		client.update(0);

		// Handle connection completion
		if (connectionThread != null && connectionThread.done)
		{
			// Rethrow any exceptions + kill the thread object
			IOException result = connectionThread.result;
			connectionThread = null;

			if (result != null)
				throw result;
		}

		// Process messages in queue
		for (;;)
		{
			ServerMessage msg = messageQueue.poll();
			if (msg == null)
				break;

			msg.receivedFromServer(this);
		}
	}

	@Override
	public void actEnd(float delta) throws IOException
	{
		// Ignore if not connected
		if (otherEndpoint == null)
			return;

		// TODO Send updates

		// Update client
		client.update(0);
	}

	@Override
	public void close() throws IOException
	{
		client.close();
	}

	/** Listener for the server (single threaded) */
	private class MyListener extends Listener
	{
		public void connected(Connection other)
		{
			otherEndpoint = other;
			// TODO handle this
		}

		public void disconnected(Connection other)
		{
			// TODO handle this
		}

		public void received(Connection other, Object obj)
		{
			// Add to message queue
			if (obj instanceof ServerMessage)
				messageQueue.add((ServerMessage) obj);
		}
	}

	/**
	 * Thread which handles connecting to the server
	 */
	private class ConnectionThread extends Thread
	{
		private final String hostname;

		public volatile boolean done;
		public IOException result;

		public ConnectionThread(String hostname)
		{
			setDaemon(true);
			setName("mp-connect");
			this.hostname = hostname;
		}

		@Override
		public void run()
		{
			try
			{
				client.connect(NetworkCommon.CONNECT_TIMEOUT, hostname, NetworkCommon.PORT);
			}
			catch (IOException e)
			{
				// Failed
				result = e;
			}

			done = true;
		}
	}
}
