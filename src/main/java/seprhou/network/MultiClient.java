package seprhou.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import seprhou.logic.Airspace;
import seprhou.logic.AirspaceObject;
import seprhou.logic.Vector2D;
import seprhou.network.message.ServerMessage;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Class which implements the client side of the multiplayer game
 */
public class MultiClient implements NetworkEndpoint
{
	private final Queue<ServerMessage> messageQueue = new LinkedList<>();

	private Client client = new Client();
	private Connection otherEndpoint;
	private ConnectionThread connectionThread;
	private IOException failException;

	private Airspace airspace;

	/**
	 * Creates a new client and opens the connection
	 *
	 * <p>You must still call actBegin / actEnd in the game loop while connecting
	 *
	 * @param hostname name of the host to connect to
	 */
	public MultiClient(String hostname)
	{
		NetworkCommon.register(client.getKryo());
		client.addListener(new MyListener());

		// Run connection attempt in new thread (so we don't block the GUI)
		connectionThread = new ConnectionThread(hostname);
		connectionThread.start();
	}

	@Override
	public NetworkEndpointState getState()
	{
		if (client == null)
			return NetworkEndpointState.CLOSED;

		if (otherEndpoint == null)
			return NetworkEndpointState.CONNECTING;

		return NetworkEndpointState.CONNECTED;
	}

	@Override
	public IOException getFailException()
	{
		return failException;
	}

	@Override
	public Airspace getAirspace()
	{
		return airspace;
	}

	@Override
	public void actBegin()
	{
		// TODO Client only: Ensure airspace.isGameOver always returns false unless the server has told us

		// Ignore if closed
		if (getState() == NetworkEndpointState.CLOSED)
			return;

		// Update client
		try
		{
			// Update server
			client.update(0);
		}
		catch (IOException e)
		{
			closeWithFail(e);
			return;
		}

		// Handle connection completion
		if (connectionThread != null && connectionThread.done)
		{
			// Store any exceptions in connect thread
			IOException result = connectionThread.result;
			connectionThread = null;

			if (result != null)
			{
				closeWithFail(result);
				return;
			}
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
	public void actEnd(float delta)
	{
		// Ignore if not connected
		if (getState() != NetworkEndpointState.CONNECTED)
			return;

		// TODO Send updates

		// Update client
		try
		{
			// Update server
			client.update(0);
		}
		catch (IOException e)
		{
			closeWithFail(e);
		}
	}

	@Override
	public void takeOff()
	{
		// Ignore if not connected
		if (getState() != NetworkEndpointState.CONNECTED)
			return;

		// TODO Implement this
	}

	@Override
	public void setTargetVelocity(AirspaceObject object, Vector2D velocity)
	{
		// Ignore if not connected
		if (getState() != NetworkEndpointState.CONNECTED)
			return;

		// TODO Implement this
	}

	@Override
	public void setTargetAltitude(AirspaceObject object, float altitude)
	{
		// Ignore if not connected
		if (getState() != NetworkEndpointState.CONNECTED)
			return;

		// TODO Implement this
	}

	@Override
	public void close()
	{
		client.close();
		client = null;
	}

	/** Close endpoint with a failiure */
	private void closeWithFail(IOException e)
	{
		failException = e;
		close();
	}

	/** Listener for the server (single threaded) */
	private class MyListener extends Listener
	{
		@Override
		public void connected(Connection other)
		{
			otherEndpoint = other;
		}

		@Override
		public void disconnected(Connection other)
		{
			close();
		}

		@Override
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
