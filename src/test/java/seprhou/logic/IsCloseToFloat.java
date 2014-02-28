package seprhou.logic;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.number.IsCloseTo;

/**
 * A matcher like IsCloseTo but for floats
 *
 * @see IsCloseTo
 */
public class IsCloseToFloat extends TypeSafeMatcher<Float>
{
	private static final float DEFAULT_EPSILON = 0.00001f;

	private final IsCloseTo proxyMatcher;

	public IsCloseToFloat(IsCloseTo proxy)
	{
		this.proxyMatcher = proxy;
	}

	@Override
	protected boolean matchesSafely(Float aFloat)
	{
		return proxyMatcher.matchesSafely(aFloat.doubleValue());
	}

	@Override
	public void describeMismatchSafely(Float item, Description mismatchDescription)
	{
		proxyMatcher.describeMismatchSafely(item.doubleValue(), mismatchDescription);
	}

	@Override
	public void describeTo(Description description)
	{
		proxyMatcher.describeTo(description);
	}

	public static Matcher<Float> closeTo(float operand)
	{
		return closeTo(operand, DEFAULT_EPSILON);
	}

	public static Matcher<Float> closeTo(float operand, float error)
	{
		return new IsCloseToFloat(new IsCloseTo(operand, error));
	}
}
