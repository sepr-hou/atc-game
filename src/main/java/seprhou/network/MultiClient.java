package seprhou.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import seprhou.logic.AirspaceObject;
import seprhou.logic.AirspaceObjectFactory;
import seprhou.logic.Rectangle;
import seprhou.logic.Vector2D;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Class which implements the client side of the multiplayer game
 */
public class MultiClient extends NetworkCommon<Client>
{
	private final Queue<ServerMessage> messageQueue = new LinkedList<>();
	private ConnectionThread connectionThread;

	/**
	 * Creates a new client and opens the connection
	 *
	 * <p>You must still call actBegin / actEnd in the game loop while connecting
	 *
	 * @param hostname name of the host to connect to
	 */
	public MultiClient(String hostname, Rectangle dimensions, AirspaceObjectFactory factory)
	{
		super(new Client(), dimensions, factory);
		kryoEndpoint.addListener(new MyListener());

		// Run connection attempt in new thread (so we don't block the GUI)
		connectionThread = new ConnectionThread(hostname);
		connectionThread.start();

		Log.info("[Client] Connecting to " + hostname);
	}

	@Override
	public void actBegin()
	{
		// TODO Client only: Ensure airspace.isGameOver always returns false unless the server has told us

		// Ignore if closed
		if (getState() == GameEndpointState.CLOSED)
			return;

		// Update client
		if (!updateEndpoint())
			return;

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

			Log.info("[Client] Connected (waiting for server to start game)");
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
		if (!isConnected())
			return;

		// Update airspace
		airspace.refresh(delta);

		// Update client
		updateEndpoint();
	}

	@Override
	public void takeOff()
	{
		// Ignore if not connected
		if (!isConnected())
			return;

		// Send takeoff request
		kryoEndpoint.sendTCP(new CMsgTakeoff());
	}

	@Override
	public void setTargetVelocity(AirspaceObject object, Vector2D velocity)
	{
		// Ignore if not connected
		if (!isConnected())
			return;

		// TODO Implement this
	}

	@Override
	public void setTargetAltitude(AirspaceObject object, float altitude)
	{
		// Ignore if not connected
		if (!isConnected())
			return;

		// Send altitude request
		kryoEndpoint.sendTCP(new CMsgSetAltitude(objectIdMap.getId(object), altitude));
	}

	/** Listener for the server (single threaded) */
	private class MyListener extends Listener
	{
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
				kryoEndpoint.connect(NetworkCommon.CONNECT_TIMEOUT, hostname, NetworkCommon.PORT);
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
