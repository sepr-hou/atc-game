/**
 * The internal game logic.
 *
 * <p>Here is a brief summary of the most important classes in this package.
 *
 * <h4>Airspace</h4>
 * <p>The {@link seprhou.logic.Airspace} class is at the root of the game
 * logic model. It contains the list of active aircraft and handles
 * collisions and updating of the game environment. Generally you would
 * run {@link seprhou.logic.Airspace#refresh(float)} every game tick and
 * then use the other methods to get data which you can use to draw the
 * game environment.
 *
 * <h4>AirspaceObject and Aircraft</h4>
 * <p>The {@link seprhou.logic.AirspaceObject} and {@link seprhou.logic.Aircraft}
 * objects live in an airspace and can move around by setting a target
 * velocity and a target altitude. Motion and collisions are controlled in the
 * AirspaceObject (so you can apply those aspects to other objects). The flight plan
 * and waypoints are handled by the Aircraft object.
 *
 * <h4>AirspaceObjectFactory</h4>
 * <p>The {@link seprhou.logic.AirspaceObjectFactory} interface is used by the
 * Airspace class to introduce new aircraft into the airspace. Each time it is
 * called, you have the option of returning a new aircraft (or any subclass of
 * {@link seprhou.logic.AirspaceObject}).
 *
 * <h4>FlightPlanGenerator</h4>
 * <p>The {@link seprhou.logic.FlightPlanGenerator} is an optional class which can
 * generate flight plans according to some configuration options. Generally you would
 * ask this class to create a flightplan in your implementation of AirspaceObjectFactory.
 */
package seprhou.logic;
