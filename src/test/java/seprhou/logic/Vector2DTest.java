package seprhou.logic;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

/**
 * Tests for {@link Vector2D}
 */
@RunWith(JUnit4.class)
public class Vector2DTest
{
    @Test
    public void correctXYValues()
    {
        Vector2D vector = new Vector2D(3.0f, 4.0f);

        assertThat(vector.getX(), is(3.0f));
        assertThat(vector.getY(), is(4.0f));
    }

    @Test
    public void correctLength()
    {
        Vector2D vector = new Vector2D(3.0f, 4.0f);

        assertThat(vector.getLength(), is(5.0f));
    }

    // TODO These are not complete !
}
