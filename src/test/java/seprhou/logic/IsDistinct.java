package seprhou.logic;

import java.util.HashSet;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

/**
 * Matcher which matches if all the items in a collection are distinct
 */
public class IsDistinct extends TypeSafeMatcher<Iterable<?>> {
	private static final IsDistinct INSTANCE = new IsDistinct();

	@Override
	protected boolean matchesSafely(Iterable<?> iterable) {
		HashSet<Object> set = new HashSet<>();

		for (Object item : iterable) {
			if (set.contains(item)) {
				return false;
			} else {
				set.add(item);
			}
		}

		return true;
	}

	@Override
	public void describeTo(Description description) {
		description.appendText("a list where all the elements are distinct");
	}

	/**
	 * Returns a matcher which matches collections containing no duplicates
	 */
	public static Matcher<Iterable<?>> distinct() {
		return IsDistinct.INSTANCE;
	}
}
