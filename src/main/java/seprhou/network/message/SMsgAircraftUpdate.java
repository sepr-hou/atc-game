package seprhou.network.message;

/**
 * Aircraft update
 *
 * <p>Sent by server to update the position of an aircraft
 */
public class SMsgAircraftUpdate implements ServerMessage
{
	// Include coordinates, whether the aircraft is turning, which direction it's turning

	@Override
	public void receivedFromServer()
	{

	}
}
