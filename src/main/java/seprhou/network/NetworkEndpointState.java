package seprhou.network;

/**
 * The state of an endpoint
 */
public enum NetworkEndpointState
{
	/** Connecting to the other endpoint (or waiting for someone to connect) */
	CONNECTING,

	/** Fully connected (+ ready to play) */
	CONNECTED,

	/** Connection died (and will never be connected again) */
	CLOSED,
}
