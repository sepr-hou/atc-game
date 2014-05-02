package seprhou.network;

import com.esotericsoftware.kryo.NotNull;
import seprhou.logic.AirspaceObject;
import seprhou.logic.Vector2D;

/**
 * Ask the server to set the target velocity of an aircraft
 *
 * <p>Invalid messages received (invalid aircraft id, target out of bounds) are ignored.
 */
class CMsgSetVelocity implements ClientMessage
{
	private int aircraftId;
	@NotNull private Vector2D newTarget;

	/**
	 * Creates a new set velocity message
	 *
	 * @param aircraftId aircraft to change speed of
	 * @param newTarget new target velocity
	 */
	public CMsgSetVelocity(int aircraftId, Vector2D newTarget)
	{
		this.aircraftId = aircraftId;
		this.newTarget = newTarget;
	}

	/** Private constructor for Kryo */
	@SuppressWarnings("unused")
	private CMsgSetVelocity()
	{
	}

	@Override
	public void receivedFromClient(MultiServer server)
	{
		if (server.isConnected())
		{
			AirspaceObject object = server.objectIdMap.getObject(aircraftId);

			if (object != null)
				object.setTargetVelocity(newTarget);
		}
	}
}
