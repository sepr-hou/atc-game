package seprhou.network;

import com.esotericsoftware.kryo.NotNull;
import seprhou.logic.Aircraft;
import seprhou.logic.AirspaceObject;
import seprhou.logic.Vector2D;

/**
 * Aircraft update
 *
 * <p>Sent by server to update the position of an aircraft
 */
class SMsgAircraftUpdate implements ServerMessage
{
	private int aircraftId;

	@NotNull private Vector2D position;
	@NotNull private Vector2D velocity;
	@NotNull private Vector2D targetVelocity;
	private float altitude, targetAltitude;

	@NotNull private TurningState turningState;

	public SMsgAircraftUpdate(int aircraftId, Aircraft aircraft, TurningState turningState)
	{
		this.aircraftId = aircraftId;
		this.position = aircraft.getPosition();
		this.velocity = aircraft.getVelocity();
		this.targetVelocity = aircraft.getTargetVelocity();
		this.altitude = aircraft.getAltitude();
		this.targetAltitude = aircraft.getTargetAltitude();
		this.turningState = turningState;
	}

	/** Private constructor for Kryo */
	@SuppressWarnings("unused")
	private SMsgAircraftUpdate()
	{
	}

	@Override
	public void receivedFromServer(MultiClient client)
	{
		if (client.isConnected())
		{
			AirspaceObject object = client.objectIdMap.getObject(aircraftId);

			if (object != null)
			{
				// Update all values
				object.setPosition(position);
				object.setVelocity(velocity);
				object.setAltitude(altitude);
				object.setTargetVelocityNoClamping(targetVelocity);
				object.setTargetAltitudeNoClamping(targetAltitude);

				// TODO turning state
			}
		}
	}
}
