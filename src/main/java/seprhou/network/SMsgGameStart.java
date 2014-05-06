package seprhou.network;

import com.esotericsoftware.kryo.NotNull;
import seprhou.logic.AircraftColour;

/**
 * Start game message
 *
 * <p>Sent by server to begin a new game (after it has ended)
 */
class SMsgGameStart implements ServerMessage
{
	private float lateral, vertical;
	@NotNull private AircraftColour yourColour;

	/**
	 * Create a new game start message
	 *
	 * @param lateral lateral separation distance (passed to new airspace)
	 * @param vertical vertical separation distance (passed to new airspace)
	 * @param yourColour the colour assigned to the CLIENT end (the receiver of this message)
	 */
	public SMsgGameStart(float lateral, float vertical, AircraftColour yourColour)
	{
		this.lateral = lateral;
		this.vertical = vertical;
		this.yourColour = yourColour;
	}

	/** Private constructor for Kryo */
	@SuppressWarnings("unused")
	private SMsgGameStart()
	{
	}

	@Override
	public void receivedFromServer(MultiClient client)
	{
		client.startGame(lateral, vertical, yourColour);
	}
}
