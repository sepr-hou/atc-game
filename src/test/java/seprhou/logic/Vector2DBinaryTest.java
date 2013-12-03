package seprhou.logic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static seprhou.logic.IsCloseToFloat.closeTo;

@RunWith(JUnit4.class)
public class Vector2DBinaryTest
{
	private static final float ELIPSON_LENGTH = Vector2DUnaryTest.ELIPSON_LENGTH;

	@Test
	public void testDistanceTo()
	{
		float correctValue = (float) (10 * Math.sqrt(2));
		Vector2D a = new Vector2D(3, 4);
		Vector2D b = new Vector2D(5, -10);

		assertThat(a.distanceTo(b), closeTo(correctValue, ELIPSON_LENGTH));
		assertThat(b.distanceTo(a), closeTo(correctValue, ELIPSON_LENGTH));
	}

	@Test
	public void testAdd()
	{
		Vector2D a = new Vector2D(3, 4);
		Vector2D b = new Vector2D(5, -10);

		assertThat(a.add(b).getX(), closeTo( 8, ELIPSON_LENGTH));
		assertThat(a.add(b).getY(), closeTo(-6, ELIPSON_LENGTH));
		assertThat(b.add(a).getX(), closeTo( 8, ELIPSON_LENGTH));
		assertThat(b.add(a).getY(), closeTo(-6, ELIPSON_LENGTH));
	}

	@Test
	public void testSub()
	{
		Vector2D a = new Vector2D(3, 4);
		Vector2D b = new Vector2D(5, -10);

		assertThat(a.sub(b).getX(), closeTo( -2, ELIPSON_LENGTH));
		assertThat(a.sub(b).getY(), closeTo( 14, ELIPSON_LENGTH));
		assertThat(b.sub(a).getX(), closeTo(  2, ELIPSON_LENGTH));
		assertThat(b.sub(a).getY(), closeTo(-14, ELIPSON_LENGTH));
	}

	@Test
	public void testMultiply()
	{
		Vector2D a = new Vector2D(5, -10);
		float b = 42;

		assertThat(a.multiply(b).getX(), closeTo( 210, ELIPSON_LENGTH));
		assertThat(a.multiply(b).getY(), closeTo(-420, ELIPSON_LENGTH));
		assertThat(a.multiply(-b).getX(), closeTo(-210, ELIPSON_LENGTH));
		assertThat(a.multiply(-b).getY(), closeTo( 420, ELIPSON_LENGTH));
	}

	@Test
	public void testMultiplyZero()
	{
		Vector2D a = new Vector2D(5, -10);

		assertThat(a.multiply(0).getX(), closeTo(0, ELIPSON_LENGTH));
		assertThat(a.multiply(0).getY(), closeTo(0, ELIPSON_LENGTH));
	}

	@Test
	public void testRotate()
	{
		Vector2D a = new Vector2D(3, 4);
		float b = (float) (Math.PI / 4);

		float ansX = (float) (-1 / Math.sqrt(2));
		float ansY = (float) ( 7 / Math.sqrt(2));

		assertThat(a.rotate( b).getX(), closeTo(ansX, ELIPSON_LENGTH));
		assertThat(a.rotate( b).getY(), closeTo(ansY, ELIPSON_LENGTH));
		assertThat(a.rotate(-b).getX(), closeTo(-ansY, ELIPSON_LENGTH));
		assertThat(a.rotate(-b).getY(), closeTo(ansX, ELIPSON_LENGTH));
	}

	@Test
	public void testRotate2()
	{
		Vector2D a = Vector2D.XAXIS;
		float b = (float) (-Math.PI / 2);

		assertThat(a.rotate( b).getX(), closeTo(0, ELIPSON_LENGTH));
		assertThat(a.rotate( b).getY(), closeTo(-1, ELIPSON_LENGTH));
		assertThat(a.rotate(-b).getX(), closeTo(0, ELIPSON_LENGTH));
		assertThat(a.rotate(-b).getY(), closeTo(1, ELIPSON_LENGTH));
	}

	@Test
	public void testEquals()
	{
		Vector2D a = new Vector2D(3, 4);
		Vector2D b = new Vector2D(5, -10);
		Vector2D c = new Vector2D(3, 4);

		assertThat(a, is(a));
		assertThat(a, is(c));
		assertThat(a, not(b));
	}
}
