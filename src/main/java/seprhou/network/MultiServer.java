package seprhou.network;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.esotericsoftware.minlog.Log;
import seprhou.logic.*;

import java.io.IOException;
import java.util.*;

/**
 * Class which implements the host side of the multiplayer game
 */
public class MultiServer extends NetworkCommon<Server>
{
	private final Queue<ClientMessage> messageQueue = new LinkedList<>();
	private final Set<AirspaceObject> newAircraft = new HashSet<>();

	private final Rectangle dimensions;
	private final FactoryProxy serverFactory;
	private final float lateral, vertical;
	private final AircraftColour myColour;

	private Connection otherEndpoint;
	private int previousScore, previousLanded;

	/**
	 * Creates and starts the server
	 */
	public MultiServer(Rectangle dimensions, AirspaceObjectFactory factory, float lateral, float vertical)
	{
		super(new Server());

		// Store airspace parameters
		this.dimensions = dimensions;
		this.serverFactory = new FactoryProxy(factory);
		this.lateral = lateral;
		this.vertical = vertical;

		// Choose random colour for myself
		this.myColour = randomColour();

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

	/**
	 * Starts a new game
	 */
	private void startGame()
	{
		// Check connected
		if (!isConnected())
			throw new IllegalStateException("cannot start game when not connected");

		// Reset airspace
		airspace = new Airspace(dimensions, serverFactory);
		airspace.setLateralSeparation(lateral);
		airspace.setVerticalSeparation(vertical);
		previousScore = 0;
		previousLanded = 0;
		objectIdMap.clear();

		// Send start message
		otherEndpoint.sendTCP(new SMsgGameStart(lateral, vertical, getOtherColour()));
	}

	/** Returns the other player's colour */
	private AircraftColour getOtherColour()
	{
		if (getMyColour() == AircraftColour.BLUE)
			return AircraftColour.RED;
		else
			return AircraftColour.BLUE;
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

		// Update airspace
		airspace.refresh(delta);

		// Update score
		if (airspace.getScore() != previousScore)
		{
			previousScore = airspace.getScore();
			otherEndpoint.sendTCP(new SMsgScoreUpdate(previousScore));
		}

		// Update landed objects
		if (airspace.getLandedObjects() != previousLanded)
		{
			previousLanded = airspace.getLandedObjects();
			otherEndpoint.sendTCP(new SMsgScoreUpdate(previousLanded));
		}

		// Handle game over
		if (airspace.isGameOver())
		{
			otherEndpoint.sendTCP(new SMsgGameEnd());

			// Update endpoint before returning so any packets are sent
			updateEndpoint();
			return;
		}

		// Send create and update messages
		for (AirspaceObject object : airspace.getActiveObjects())
		{
			// Ignore non aircraft
			if (object instanceof Aircraft)
			{
				Aircraft aircraft = (Aircraft) object;
				ServerMessage msg;

				if (newAircraft.contains(aircraft))
				{
					// Create id for aircraft and send create message
					msg = new SMsgAircraftCreate(objectIdMap.createWithNewId(aircraft), aircraft);
				}
				else
				{
					// Create update message
					msg = new SMsgAircraftUpdate(objectIdMap.getId(aircraft), aircraft);
				}

				otherEndpoint.sendTCP(msg);
			}
		}

		// Send destroy messages
		for (AirspaceObject object : airspace.getCulledObjects())
		{
			// Ignore non aircraft
			if (object instanceof Aircraft)
			{
				Aircraft aircraft = (Aircraft) object;
				int id = objectIdMap.getId(aircraft);

				// Send message and destroy in map
				objectIdMap.destroy(id);
				otherEndpoint.sendTCP(new SMsgAircraftDestroy(id));
			}
		}

		// Reset new aircraft list
		newAircraft.clear();

		// Update server
		updateEndpoint();
	}

	@Override
	public void takeOff()
	{
		// Ignore if not connected
		if (!isConnected())
			return;

		airspace.takeOff();
	}

	@Override
	public void handover(Aircraft aircraft)
	{
		// Ignore if not connected
		if (!isConnected())
			return;

		aircraft.setColour(getOtherColour());
	}

	@Override
	public void setTargetVelocity(AirspaceObject object, Vector2D velocity)
	{
		// Ignore if not connected
		if (!isConnected())
			return;

		object.setTargetVelocity(velocity);
	}

	@Override
	public void setTargetAltitude(AirspaceObject object, float altitude)
	{
		// Ignore if not connected
		if (!isConnected())
			return;

		object.setTargetAltitude(altitude);
	}

	/** Returns a random multiplayer colour */
	private static AircraftColour randomColour()
	{
		if (Utils.getRandom().nextBoolean())
			return AircraftColour.BLUE;
		else
			return AircraftColour.RED;
	}

	/** AirspaceObjectFactory which handles multiplayer specific stuff */
	private class FactoryProxy implements AirspaceObjectFactory
	{
		private final AirspaceObjectFactory userFactory;

		public FactoryProxy(AirspaceObjectFactory userFactory)
		{
			this.userFactory = userFactory;
		}

		@Override
		public AirspaceObject makeObject(Airspace airspace, FlightPlan flightPlan, String flightNumber, AircraftColour dummy)
		{
			// Create the object (note we use a new colour here)
			AirspaceObject object = userFactory.makeObject(airspace, flightPlan, flightNumber, randomColour());

			if (object != null)
				newAircraft.add(object);

			return object;
		}
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
			startGame();
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
