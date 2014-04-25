package seprhou.network.message;

import seprhou.logic.Aircraft;
import seprhou.logic.Vector2D;
import seprhou.network.MultiClient;

/**
 * Aircraft update
 *
 * <p>Sent by server to update the position of an aircraft
 */
public class SMsgAircraftUpdate implements ServerMessage
{
	private int aircraftId;

	private Vector2D position, velocity, targetVelocity;
	private float altitude, targetAltitude;
	private TurningState turningState;

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
	private SMsgAircraftUpdate()
	{
	}

	@Override
	public void receivedFromServer(MultiClient client)
	{

	}
}
