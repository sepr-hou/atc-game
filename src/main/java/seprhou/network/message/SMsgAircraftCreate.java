package seprhou.network.message;

import com.esotericsoftware.kryo.NotNull;
import seprhou.logic.Aircraft;
import seprhou.logic.FlightPlan;

/**
 * New aircraft message
 *
 * <p>Sent by server when a new aircraft enters the game
 */
public class SMsgAircraftCreate implements ServerMessage
{
	private int id;
	@NotNull private String name;
	@NotNull private FlightPlan flightPlan;

	/**
	 * Create a new aircraft create message
	 *
	 * @param id id of new aircraft
	 * @param aircraft aircraft which was created
	 */
	public SMsgAircraftCreate(int id, Aircraft aircraft)
	{
		this.id = id;
		this.name = aircraft.getName();
		this.flightPlan = aircraft.getFlightPlan();
	}

	/** Private constructor for Kryo */
	private SMsgAircraftCreate()
	{
	}

	@Override
	public void receivedFromServer()
	{

	}
}
