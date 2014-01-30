package seprhou.logic;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Various utility functions
 */
public final class Utils {
	private static final Random random = new Random();

	/**
	 * Returns a global random number generator
	 * 
	 * <p>
	 * Note this is not thread safe and may not return the same generator all
	 * the time
	 */
	public static Random getRandom() {
		return Utils.random;
	}

	/**
	 * Generates a random subset of the given list
	 * 
	 * @param list list of items
	 * @param n number of items to choose
	 * @param <T> type of the items in the list
	 * @return the chosen item
	 */
	public static <T> List<T> randomSubset(List<T> list, int n) {
		// Can't get more items than we actually have
		if (n > list.size()) {
			throw new IllegalArgumentException("n cannot be greater than the size of the list");
		}

		// Copy the list and shuffle it
		List<T> result = new ArrayList<>(list);
		Collections.shuffle(result, Utils.random);

		// Return first n items
		return result.subList(0, n);
	}

	/**
	 * Chooses an item from a list
	 * 
	 * @param list list to choose from
	 * @param <T> type of the items in the list
	 * @return the chosen item
	 */
	public static <T> T randomItem(List<T> list) {
		return list.get(Utils.random.nextInt(list.size()));
	}

	/**
	 * Chooses an item from a list but does not include the invalidItem
	 * 
	 * @param list list to choose from
	 * @param invalidItem the item which will not be picked
	 * @param <T> type of the items in the list
	 * @return the chosen item
	 */
	public static <T> T randomItem(List<T> list, T invalidItem) {
		// Check for impossible situation
		if (list.size() == 1 && list.get(0) == invalidItem) {
			throw new IllegalArgumentException("list given to randomItem contains no valid items!");
		}

		// Choose one item and skip it if it's invalid
		int item = Utils.random.nextInt(list.size());
		if (list.get(item) == invalidItem) {
			item = (item + 1) % list.size();
		}

		return list.get(item);
	}

	/**
	 * Formats a time value measured in seconds into a string showing elapsed
	 * time
	 * 
	 * @param time elapsed time in seconds
	 * @return a string representation of the elapsed time
	 */
	public static String formatTime(float time) {
		// Separate into minutes, seconds and tenths
		int minutes = (int) time / 60;
		int seconds = (int) time % 60;
		int tenths = (int) (time * 10) % 10;

		return String.format("%02d:%02d.%d", minutes, seconds, tenths);
	}

	private Utils() {}
}
