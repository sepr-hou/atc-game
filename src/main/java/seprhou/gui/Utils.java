package seprhou.gui;

/**
 * Various utility functions
 */
public final class Utils
{
	/**
	 * Formats a time value measured in seconds into a string showing elapsed time
	 *
	 * @param time elapsed time in seconds
	 * @return a string representation of the elapsed time
	 */
	public static String formatTime(float time)
	{
		// Separate into minutes, seconds and tenths
		int minutes = ((int) time) / 60;
		int seconds = ((int) time) % 60;
		int tenths =  ((int) (time * 10)) % 10;

		return String.format("%02d:%02d.%d", minutes, seconds, tenths);
	}

	private Utils()
	{
	}
}
