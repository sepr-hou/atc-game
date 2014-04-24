package seprhou.network.message;

import com.esotericsoftware.kryo.Kryo;
import com.esotericsoftware.kryo.Serializer;
import com.esotericsoftware.kryo.io.Input;
import com.esotericsoftware.kryo.io.Output;
import seprhou.logic.FlightPlan;
import seprhou.logic.Vector2D;

import java.util.Arrays;
import java.util.List;

/**
 * Class which handles kryo message registration
 */
public final class MessageSetup
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
		kryo.register(SMsgAircraftCreate.class);
		kryo.register(SMsgAircraftUpdate.class);
		kryo.register(SMsgGameEnd.class);
		kryo.register(SMsgGameStart.class);
		kryo.register(SMsgScoreUpdate.class);
		kryo.register(SMsgVersion.class);

		// Register extra classes (used by messages)
		kryo.register(TurningState.class);
		kryo.register(FlightPlan.class, new FlightPlanSerializer());
		kryo.register(Vector2D.class, new Vector2DSerializer());
		kryo.register(Vector2D[].class);
	}

	private MessageSetup()
	{
	}

	/** Manual serializer for Vector2D */
	private static class Vector2DSerializer extends Serializer<Vector2D>
	{
		{
			setImmutable(true);
		}

		@Override
		public void write(Kryo kryo, Output output, Vector2D object)
		{
			output.writeFloat(object.getX());
			output.writeFloat(object.getY());
		}

		@Override
		public Vector2D read(Kryo kryo, Input input, Class<Vector2D> type)
		{
			float x = input.readFloat();
			float y = input.readFloat();
			return new Vector2D(x, y);
		}
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
