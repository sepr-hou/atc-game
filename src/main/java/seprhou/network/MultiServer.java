package seprhou.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import seprhou.logic.*;

import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;

/**
 * Class which implements the host side of the multiplayer game
 */
public class MultiServer extends NetworkCommon<Server>
{
	private final Queue<ClientMessage> messageQueue = new LinkedList<>();
	private Connection otherEndpoint;

	/**
	 * Creates and starts the server
	 */
	public MultiServer(Rectangle dimensions, AirspaceObjectFactory factory, float lateral, float vertical)
	{
		super(new Server(), dimensions, factory);

		// Create airspace
		airspace = new Airspace(dimensions, factory);
		airspace.setLateralSeparation(lateral);
		airspace.setVerticalSeparation(vertical);

		// Setup server connection
		kryoEndpoint.addListener(new MyListener());

		try
		{
			// Update server
			kryoEndpoint.bind(PORT);
		}
		catch (IOException e)
		{
			closeWithFail(e);
		}

		Log.info("[Server] Listening on port " + PORT);
	}

	@Override
	public void actBegin()
	{
		// Ignore if closed
		if (getState() == GameEndpointState.CLOSED)
			return;

		// Update server
		if (!updateEndpoint())
			return;

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
	public void actEnd(float delta)
	{
		// Ignore if not connected
		if (!isConnected())
			return;

		// TODO send updates

		// Update airspace
		airspace.refresh(delta);

		// Update server
		updateEndpoint();
	}

	@Override
	public void takeOff()
	{
		// Ignore if not connected
		if (!isConnected())
			return;

		// TODO Implement this
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

		// TODO Implement this
	}

	/** Listener for the server (single threaded) */
	private class MyListener extends Listener
	{
		@Override
		public void connected(Connection other)
		{
			// Reject if not connecting
			if (getState() != GameEndpointState.CONNECTING)
				other.close();

			// Initialize internal state
			Log.info("[Server] Client " + other.getRemoteAddressTCP() + " has connected");
			otherEndpoint = other;
			state = GameEndpointState.CONNECTED;

			// Start game messages
			otherEndpoint.sendTCP(new SMsgVersion());
			otherEndpoint.sendTCP(new SMsgGameStart(airspace.getLateralSeparation(), airspace.getVerticalSeparation()));
		}

		@Override
		public void disconnected(Connection other)
		{
			if (getState() == GameEndpointState.CONNECTED && other == otherEndpoint)
			{
				Log.info("[Server] Client has disconnected");
				otherEndpoint = null;

				// Close entire server to prevent more connections
				close();
			}
		}

		@Override
		public void received(Connection other, Object obj)
		{
			// Add to message queue
			if (obj instanceof ClientMessage)
				messageQueue.add((ClientMessage) obj);
		}
	}
}
