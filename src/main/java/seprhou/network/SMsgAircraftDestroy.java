package seprhou.network;

import seprhou.logic.Airspace;
import seprhou.logic.AirspaceObject;

/**
 * Aircraft destroyed message
 *
 * <p>Sent by server when an aircraft leaves the game (out of bounds, landed)
 */
class SMsgAircraftDestroy implements ServerMessage
{
	private int id;

	/**
	 * Create a new aircraft destroy message
	 *
	 * @param id id of destroyed aircraft
	 */
	public SMsgAircraftDestroy(int id)
	{
		this.id = id;
	}

	/** Private constructor for Kryo */
	@SuppressWarnings("unused")
	private SMsgAircraftDestroy()
	{
	}

	@Override
	public void receivedFromServer(MultiClient client)
	{
		if (client.isConnected())
		{
			AirspaceObject object = client.objectIdMap.getObject(id);
			if (object != null)
			{
				// TODO landed planes handling??
				// Move object to culled list
				Airspace airspace = client.getAirspace();

				airspace.getActiveObjects().remove(object);
				airspace.getCulledObjects().add(object);
			}
		}
	}
}
