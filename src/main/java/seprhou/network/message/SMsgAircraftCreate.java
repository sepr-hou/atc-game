package seprhou.network.message;

import com.esotericsoftware.kryo.NotNull;
import seprhou.logic.Aircraft;
import seprhou.logic.FlightPlan;
import seprhou.network.MultiClient;

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
		this(id, aircraft.getName(), aircraft.getFlightPlan());
	}

	/**
	 * Create a new aircraft create message
	 *
	 * @param id id of new aircraft
	 * @param name name of the aircraft
	 * @param flightPlan flight plan of the aircraft
	 */
	public SMsgAircraftCreate(int id, String name, FlightPlan flightPlan)
	{
		if (name == null || flightPlan == null)
			throw new IllegalArgumentException("name and flightPlan must be non-null");

		this.id = id;
		this.name = name;
		this.flightPlan = flightPlan;
	}

	/** Private constructor for Kryo */
	private SMsgAircraftCreate()
	{
	}

	@Override
	public void receivedFromServer(MultiClient client)
	{

	}
}