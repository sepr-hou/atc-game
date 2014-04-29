package seprhou.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.kryonet.EndPoint;
import com.esotericsoftware.minlog.Log;
import seprhou.logic.*;
import seprhou.network.message.*;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Class which handles common network initialization tasks
 */
public abstract class NetworkCommon<TEndPoint extends EndPoint> implements GameEndpoint
{
	/**
	 * Version of the protocol
	 *
	 * <p>This ensures that both the client and server agree on the format of messages passed between them.
	 * <p>This MUST be incremented when:
	 * <ul>
	 *     <li>Adding or removing any messages</li>
	 *     <li>Adding, removing or changing the type of any field in a message</li>
	 *     <li>Changing the order of message registration</li>
	 * </ul>
	 */
	public static final int PROTOCOL_VERSION = 1;

	/** TCP port number to listen / connect on */
	public static final int PORT = 59873;

	/** TCP connect timeout in milliseconds */
	protected static final int CONNECT_TIMEOUT = 5000;

	protected final TEndPoint kryoEndpoint;
	protected final Rectangle dimensions;
	protected final AirspaceObjectFactory userFactory;

	protected GameEndpointState state = GameEndpointState.CONNECTING;
	protected IOException failException;
	protected Airspace airspace;

	/**
	 * Initializes a common network endpoint
	 *
	 * @param kryoEndpoint the kryo endpoint object to use
	 * @param dimensions the dimensions of the airspace
	 * @param factory the factory class used to create aircraft
	 */
	public NetworkCommon(TEndPoint kryoEndpoint, Rectangle dimensions, AirspaceObjectFactory factory)
	{
		// Setup final fields
		this.kryoEndpoint = kryoEndpoint;
		this.dimensions = dimensions;
		this.userFactory = factory;

		// Set log level
		Log.set(Log.LEVEL_DEBUG);

		// Register kryo objects
		register(kryoEndpoint.getKryo());
	}

	@Override
	public void close()
	{
		kryoEndpoint.close();
		state = GameEndpointState.CLOSED;
	}

	/** Close connection and set fail exception */
	protected void closeWithFail(IOException e)
	{
		failException = e;
		close();
	}

	@Override
	public GameEndpointState getState()
	{
		return state;
	}

	/** Returns true if this endpoint is connected */
	public boolean isConnected()
	{
		return getState() == GameEndpointState.CONNECTED;
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

	/** Runs update(0) on the endpoint */
	protected boolean updateEndpoint()
	{
		try
		{
			kryoEndpoint.update(0);
			return true;
		}
		catch (IOException e)
		{
			closeWithFail(e);
			return false;
		}
	}

	/**
	 * Registers messages with kryo
	 *
	 * @param kryo kryo object to register with
	 */
	private static void register(Kryo kryo)
	{
		// READ PROTOCOL_VERSION JAVADOC BEFORE CHANGING THIS METHOD
		kryo.setReferences(false);
		kryo.setRegistrationRequired(true);
		kryo.setAutoReset(true);

		// Register all message classes
		kryo.register(CMsgSetAltitude.class);
		kryo.register(CMsgSetSpeed.class);
		kryo.register(CMsgSetTurning.class);

		kryo.register(SMsgAircraftCreate.class);
		kryo.register(SMsgAircraftDestroy.class);
		kryo.register(SMsgAircraftUpdate.class);
		kryo.register(SMsgGameEnd.class);
		kryo.register(SMsgGameStart.class);
		kryo.register(SMsgScoreUpdate.class);
		kryo.register(SMsgVersion.class);

		// Register extra classes (used by messages)
		kryo.register(TurningState.class);
		kryo.register(FlightPlan.class, new FlightPlanSerializer());
		kryo.register(Vector2D.class);
		kryo.register(Vector2D[].class);
	}

	/** Manual serializer for FlightPlan */
	private static class FlightPlanSerializer extends Serializer<FlightPlan>
	{
		{
			setImmutable(true);
		}

		@Override
		public void write(Kryo kryo, Output output, FlightPlan object)
		{
			List<Vector2D> waypoints = object.getWaypoints();
			kryo.writeObject(output, waypoints.toArray(new Vector2D[waypoints.size()]));

			output.writeFloat(object.getInitialSpeed());
			output.writeFloat(object.getInitialAltitude());
			output.writeBoolean(object.isLanding());
			output.writeBoolean(object.isStartOnRunway());
		}

		@Override
		public FlightPlan read(Kryo kryo, Input input, Class<FlightPlan> type)
		{
			List<Vector2D> waypoints = Arrays.asList(kryo.readObject(input, Vector2D[].class));

			float initialSpeed = input.readFloat();
			float initialAltitude = input.readFloat();
			boolean landing = input.readBoolean();
			boolean runway = input.readBoolean();

			return new FlightPlan(waypoints, initialSpeed, initialAltitude, landing, runway);
		}
	}
}
