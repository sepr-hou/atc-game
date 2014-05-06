package seprhou.network;

import seprhou.logic.Aircraft;
import seprhou.logic.AirspaceObject;

/**
 * Ask the server to handover a plane
 */
class CMsgHandover implements ClientMessage
{
	private int aircraftId;

	/**
	 * Creates a new handover message
	 *
	 * @param aircraftId aircraft to handover
	 */
	public CMsgHandover(int aircraftId)
	{
		this.aircraftId = aircraftId;
	}

	/** Private constructor for Kryo */
	@SuppressWarnings("unused")
	private CMsgHandover()
	{
	}

	@Override
	public void receivedFromClient(MultiServer server)
	{
		if (server.isConnected())
		{
			AirspaceObject object = server.objectIdMap.getObject(aircraftId);

			if (object != null && object instanceof Aircraft)
			{
				Aircraft aircraft = (Aircraft) object;
				aircraft.setColour(server.getMyColour());
			}
		}
	}
}
