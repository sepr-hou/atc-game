package seprhou.network;

import seprhou.logic.*;

import java.io.IOException;

/**
 * A single player endpoint which simply passes all events to the airspace
 */
public class SinglePlayer implements GameEndpoint
{
	private final Airspace airspace;

	/**
	 * Creates a new single player endpoint
	 *
	 * @param dimensions game dimensions
	 * @param factory aircraft factory
	 * @param lateral lateral separation
	 * @param vertical lateral separation
	 */
	public SinglePlayer(Rectangle dimensions, AirspaceObjectFactory factory, float lateral, float vertical)
	{
		airspace = new Airspace(dimensions, factory);
		airspace.setLateralSeparation(lateral);
		airspace.setVerticalSeparation(vertical);
	}

	@Override
	public Airspace getAirspace()
	{
		return airspace;
	}

	// Airspace forwarding methods
	@Override
	public void actEnd(float delta)
	{
		airspace.refresh(delta);
	}

	@Override
	public void takeOff()
	{
		airspace.takeOff();
	}

	@Override
	public void setTargetVelocity(AirspaceObject object, Vector2D velocity)
	{
		object.setTargetVelocity(velocity);
	}

	@Override
	public void setTargetAltitude(AirspaceObject object, float altitude)
	{
		object.setTargetAltitude(altitude);
	}

	// No-op methods
	@Override
	public void actBegin() { }

	@Override
	public GameEndpointState getState() { return GameEndpointState.CONNECTED; }

	@Override
	public IOException getFailException() { return null; }

	@Override
	public void close() { }
}
