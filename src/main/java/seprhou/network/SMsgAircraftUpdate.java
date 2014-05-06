package seprhou.network;

import com.esotericsoftware.kryo.NotNull;
import seprhou.logic.Aircraft;
import seprhou.logic.AircraftColour;
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
	@NotNull private AircraftColour colour;

	public SMsgAircraftUpdate(int aircraftId, Aircraft aircraft)
	{
		this.aircraftId = aircraftId;
		this.position = aircraft.getPosition();
		this.velocity = aircraft.getVelocity();
		this.targetVelocity = aircraft.getTargetVelocity();
		this.altitude = aircraft.getAltitude();
		this.targetAltitude = aircraft.getTargetAltitude();
		this.colour = aircraft.getColour();
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

			if (object != null && object instanceof Aircraft)
			{
				Aircraft aircraft = (Aircraft) object;

				// Update all values
				aircraft.setPosition(position);
				aircraft.setVelocity(velocity);
				aircraft.setAltitude(altitude);
				aircraft.setTargetVelocityNoClamping(targetVelocity);
				aircraft.setTargetAltitudeNoClamping(targetAltitude);
				aircraft.setColour(colour);
			}
		}
	}
}
