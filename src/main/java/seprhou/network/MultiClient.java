package seprhou.network;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.minlog.Log;
import seprhou.logic.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Class which implements the client side of the multiplayer game
 */
public class MultiClient extends NetworkCommon<Client>
{
	private final Queue<ServerMessage> messageQueue = new LinkedList<>();

	private final Rectangle dimensions;
	final AirspaceObjectFactory factory;

	private ConnectionThread connectionThread;
	private AircraftColour myColour;
	boolean serverGameOver;		// Server's perspective on if the game is over or not

	/**
	 * Creates a new client and opens the connection
	 *
	 * <p>You must still call actBegin / actEnd in the game loop while connecting
	 *
	 * @param hostname name of the host to connect to
	 */
	public MultiClient(String hostname, Rectangle dimensions, AirspaceObjectFactory factory)
	{
		super(new Client());
		kryoEndpoint.addListener(new MyListener());

		// Store airspace properties for later
		this.dimensions = dimensions;
		this.factory = factory;

		// Run connection attempt in new thread (so we don't block the GUI)
		connectionThread = new ConnectionThread(hostname);
		connectionThread.start();

		Log.info("[Client] Connecting to " + hostname);
	}

	/** Start the game */
	void startGame(float lateral, float vertical, AircraftColour colour)
	{
		// Must not be closed + must have a connection
		if (getState() == GameEndpointState.CLOSED || !kryoEndpoint.isConnected())
			return;

		// Initialize airspace
		//  Use null factory to prevent aircraft being created
		airspace = new Airspace(dimensions, null);
		airspace.setLateralSeparation(lateral);
		airspace.setVerticalSeparation(vertical);
		serverGameOver = false;
		myColour = colour;
		objectIdMap.clear();

		// Mark as connected
		state = GameEndpointState.CONNECTED;
		Log.info("[Client] Game started");
	}

	@Override
	public AircraftColour getMyColour()
	{
		return myColour;
	}

	@Override
	public void actBegin()
	{
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

		if (isConnected())
		{
			// Force game over
			airspace.setGameOver(serverGameOver);

			// Force landing planes to 0
			//  Disables landing planes limit in Aircraft.refresh (controlled by server)
			airspace.setLandingPlanes(0);
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
	public void handover(Aircraft object)
	{
		// Ignore if not connected
		if (!isConnected())
			return;

		// Send velocity request
		kryoEndpoint.sendTCP(new CMsgHandover(objectIdMap.getId(object)));
	}

	@Override
	public void setTargetVelocity(AirspaceObject object, Vector2D velocity)
	{
		// Ignore if not connected
		if (!isConnected())
			return;

		// Send velocity request
		kryoEndpoint.sendTCP(new CMsgSetVelocity(objectIdMap.getId(object), velocity));
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
