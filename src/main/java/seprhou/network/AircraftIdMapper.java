package seprhou.network;

import seprhou.logic.AirspaceObject;

/**
 * Aircraft to id mapping
 *
 * <p>Conversions both ways (id to aircraft, aircraft to id) should be "fast".
 */
class AircraftIdMapper
{
	/**
	 * Creates a new id to aircraft mapping
	 *
	 * @param id id of the aircraft
	 * @param aircraft aircraft to add
	 */
	public void create(int id, AirspaceObject aircraft)
	{
		// TODO Implement this
	}

	/**
	 * Creates a new id to aircraft mapping
	 *
	 * <p>All ids returned by this method will be unique for the entire lifetime of the class
	 *
	 * @param aircraft aircraft to add
	 * @return the new id of the aircraft
	 */
	public int createWithNewId(AirspaceObject aircraft)
	{
		// TODO Implement this
		return 0;
	}

	/**
	 * Destroys the mapping with the given id
	 *
	 * @param id aircraft id
	 */
	public void destroy(int id)
	{
		// TODO Implement this
	}

	/**
	 * Clear all the id mappings
	 *
	 * <p>Ids previously used are allowed to be reused after calling this method
	 */
	public void clear()
	{
		// TODO Implement this
	}

	/**
	 * Gets the id of the given aircraft
	 *
	 * @param object aircraft to get id of
	 * @return the aircraft id
	 */
	public int getId(AirspaceObject object)
	{
		// TODO Implement this
		return 0;
	}

	/**
	 * Gets the aircraft with the given id
	 *
	 * @param id aircraft id
	 * @return the mapped object or null if it does not exist
	 */
	public AirspaceObject getObject(int id)
	{
		// TODO Implement this
		return null;
	}
}
