package seprhou.logic;

/**
 * Interface responsible for creating aircraft when the Airspace requests it
 */
public interface AirspaceObjectFactory
{
	/**
	 * Creates a new airspace object
	 *
	 * <p>
	 * This method is called when the airspace needs to generate a new aircraft.
	 * This can occur when:
	 * <ul>
	 *     <li>Every so often (randomly) to introduce a new aircraft</li>
	 *     <li>When an aircraft needs to takeoff</li>
	 * </ul>
	 *
	 * If this method returns {@code null}, no aircraft will be added and any
	 * takeoff command is ignored.
	 *
	 * @param airspace the airspace the object will be created in
	 * @param flightPlan flight plan to generate aircraft with
	 * @return the new object or {@code null} to create no object
	 */
	public AirspaceObject makeObject(Airspace airspace, FlightPlan flightPlan);
}
