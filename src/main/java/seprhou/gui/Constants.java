package seprhou.gui;

import java.util.Arrays;
import java.util.List;

import seprhou.logic.Runway;
import seprhou.logic.Vector2D;

/**
 * Class containing the game constants
 */
public final class Constants {
	// #################
	// GUI options
	// #################

	/** Amount the altitude jumps to when a key is pressed */
	public static final float ALTITUDE_JUMP = 10000;

	// #################
	// Global game configuration options
	// #################

	/** Minimum lateral separation for collision warnings */
	// public static final float LATERAL_SEPARATION = 200.0f;
	/** Minimum vertical separation for collision warnings */
	// public static final float VERTICAL_SEPARATION = 1000.0f;

	/** List of available waypoints */
	public static final List<Vector2D> WAYPOINTS = Arrays.asList(new Vector2D(
			280, 210), new Vector2D(280, 420), new Vector2D(280, 630),
			new Vector2D(280, 840), new Vector2D(560, 210), new Vector2D(560,
					420), new Vector2D(560, 630), new Vector2D(560, 840),
			new Vector2D(840, 210), new Vector2D(840, 420), new Vector2D(840,
					840), new Vector2D(1120, 210), new Vector2D(1120, 420),
			new Vector2D(1120, 630), new Vector2D(1120, 840));

	/** List of available entry and exit points */
	public static final List<Vector2D> ENTRY_EXIT_POINTS = Arrays.asList(
			new Vector2D(100, 0), new Vector2D(0, 800), new Vector2D(1000, 0));

	public static final List<Runway> RUNWAYS = Arrays.asList(new Runway(
			new Vector2D(766.5f, 682.5f), new Vector2D(1018.5f, 426.3f)),
			new Runway(new Vector2D(722.4f, 508.2f), new Vector2D(877.8f,
					663.6f)));

	/** Set of altitudes new flights are generated using */
	public static final List<Float> INITIAL_ALTITUDES = Arrays.asList(10000f,
			20000f, 30000f);

	/** Set of speeds new flights are generated using */
	public static final List<Float> INITIAL_SPEEDS = Arrays.asList(50f, 60f,
			40f);

	/** Minimum free radius needed for an aircraft to enter at an entry point */
	public static final float MIN_SAFE_ENTRY_DISTANCE = 200;

	/** Minimum time between generated aircraft (seconds) */
	public static final float MIN_TIME_BETWEEN_AIRCRAFT = 3;

	/** Average number of aircraft generated per second */
	public static final float AIRCRAFT_PER_SEC = 0.2f;

	/** Maximum number of on screen aircraft */
	public static final int MAX_AIRCRAFT = 10;

	/** Minimum number of waypoints in each flight plan */
	public static final int MIN_WAYPOINTS = 2;

	/** Maximum number of waypoints in each flight plan */
	public static final int MAX_WAYPOINTS = 4;

	// #################
	// ConcreteAircraft options (some other options are derived from the above
	// options)
	// #################

	/**
	 * Size of the aircraft (radius of the circle which represents the aircraft)
	 */
	public static final float AIRCRAFT_SIZE = 32;

	/** Ascent / decent rate of the aircraft (units per second) */
	public static final float AIRCRAFT_ASCENT_RATE = 2000;

	/** Maximum turn rate of the aircraft (radians per second) */
	public static final float AIRCRAFT_TURN_RATE = 1.25f;
	
	public static final List<Vector2D> LANDED_AIRCRAFT_POSITIONS = Arrays.asList(new Vector2D(880, 400), new Vector2D(800, 350));
	public static final List<Float> LANDED_AIRCRAFT_ANGLES = Arrays.asList(40f, 40f);

	private Constants() {}
}
