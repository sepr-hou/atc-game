package seprhou.network;

import seprhou.logic.Airspace;
import seprhou.logic.AirspaceObject;
import seprhou.logic.Vector2D;

import java.io.Closeable;
import java.io.IOException;

/**
 * An endpoint of the game
 *
 * <p>This interface allows the GUI code to interface with the network code without worrying
 * about if we're the server, client or on a single player game.
 */
public interface GameEndpoint extends Closeable
{
	/** Closes the network endpoint */
	@Override
	void close();

	/**
	 * Returns the state of the endpoint
	 */
	GameEndpointState getState();

	/**
	 * Returns the exception which caused the connection to close
	 *
	 * <p>If {@link #getState()} returns {@link GameEndpointState#CLOSED}, then you can
	 * call this method to get the reason. Often, this method will still return null if
	 * no extra information can be obtained.
	 */
	IOException getFailException();

	/**
	 * Returns the airspace controlled by this endpoint
	 *
	 * <p>Returns NULL if no game is active (eg connecting, waiting for connection, game ended, etc)
	 *
	 * @return the active airspace or null if no game is active
	 */
	Airspace getAirspace();

	/**
	 * Method called at the start of the global act method
	 *
	 * <p>You should call this method once per frame, before acting on anything in the airspace.
	 * <p>This method will receive + process messages from the other endpoint
	 */
	void actBegin();

	/**
	 * Method called at the end of the global act method
	 *
	 * <p>You should call this method once per frame, after acting on anything in the airspace.
	 * <p>This method will refresh the airspace and send updates / requests to the other endpoint
	 *
	 * @param delta time since last frame (seconds)
	 */
	void actEnd(float delta);

	/**
	 * Ask one aircraft to take off
	 *
	 * <p>The request may not take affect immediately (it may need a network roundtrip)
	 */
	void takeOff();

	/**
	 * Sets the target velocity of an aircraft
	 *
	 * <p>The request may not take affect immediately (it may need a network roundtrip)
	 *
	 * @param object object to change
	 * @param velocity new velocity
	 */
	void setTargetVelocity(AirspaceObject object, Vector2D velocity);

	/**
	 * Sets the target altitude of an aircraft
	 *
	 * <p>The request may not take affect immediately (it may need a network roundtrip)
	 *
	 * @param object object to change
	 * @param altitude new altitude
	 */
	void setTargetAltitude(AirspaceObject object, float altitude);
}
