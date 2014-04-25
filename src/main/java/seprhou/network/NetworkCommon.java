package seprhou.network;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import com.esotericsoftware.minlog.Log;
import seprhou.logic.FlightPlan;
import seprhou.logic.Vector2D;
import seprhou.network.message.*;

import java.util.Arrays;
import java.util.List;

/**
 * Class which handles common network initialization tasks
 */
public final class NetworkCommon
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

	/**
	 * TCP port number to listen / connect on
	 */
	public static final int PORT = 59873;

	/**
	 * TCP connect timeout in milliseconds
	 */
	public static final int CONNECT_TIMEOUT = 5000;

	/**
	 * Setup the global kryo log level
	 */
	public static void setupLogger()
	{
		Log.set(Log.LEVEL_DEBUG);
	}

	/**
	 * Registers messages with kryo
	 *
	 * @param kryo kryo object to register with
	 */
	public static void register(Kryo kryo)
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

	private NetworkCommon()
	{
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
