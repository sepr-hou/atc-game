package seprhou.network;

import java.util.HashMap;
import java.util.Map;

import seprhou.logic.AirspaceObject;

/**
 * Aircraft to id mapping
 *
 * <p>Conversions both ways (id to aircraft, aircraft to id) should be "fast".
 */
class AircraftIdMapper
{
	private final Map <Integer, AirspaceObject> idToObject = new HashMap<>();
	private final Map <AirspaceObject, Integer> objectToId = new HashMap<>();
	
	private int counter = 0; 
	/**
	 * Creates a new id to aircraft mapping
	 *
	 * @param id id of the aircraft
	 * @param aircraft aircraft to add
	 */
	public void create(int id, AirspaceObject aircraft)
	{
		idToObject.put(id, aircraft);
		objectToId.put(aircraft, id);
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
		counter += 1;
		create(counter, aircraft);
		return counter;
	}

	/**
	 * Destroys the mapping with the given id
	 *
	 * @param id aircraft id
	 */
	public void destroy(int id)
	{
		objectToId.remove(idToObject.get(id));
		idToObject.remove(id);
	}

	/**
	 * Clear all the id mappings
	 *
	 * <p>Ids previously used are allowed to be reused after calling this method
	 */
	public void clear()
	{
		objectToId.clear();
		idToObject.clear();
	}

	/**
	 * Gets the id of the given aircraft
	 *
	 * @param object aircraft to get id of
	 * @return the aircraft id
	 */
	public int getId(AirspaceObject object)
	{
		return objectToId.get(object);
	}

	/**
	 * Gets the aircraft with the given id
	 *
	 * @param id aircraft id
	 * @return the mapped object or null if it does not exist
	 */
	public AirspaceObject getObject(int id)
	{
		return idToObject.get(id);
	}
}
