package seprhou.logic;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Assert;
import org.junit.Test;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

/**
 * Tests for {@link Utils}
 * 
 * <p>
 * This class contains tests against random list / item generators. Obviously we
 * cannot actually compare the list exactly since it's supposed to be random.
 * Here we just test the basic constraints on the function since "randomness" is
 * almost impossible to test.
 */
@RunWith(Enclosed.class)
public class UtilsTest {
	/** Collection of lists used for testing */
	private static final List<Integer> EMPTY_LIST = Collections.emptyList();
	private static final List<Integer> ONE_ITEM_LIST = Arrays.asList(0);
	private static final List<Integer> TEST_LIST = Arrays.asList(1, 2, 3, 4, 5,
			-1, -2, -3, -4, 100, 200);

	@RunWith(JUnit4.class)
	public static class RandomSubset {
		private static void doRandomSubsetTest(List<Integer> testList, int items) {
			List<Integer> subset = Utils.randomSubset(testList, items);

			// Test list size + that it is a subset
			Assert.assertThat(subset, Matchers.hasSize(items));
			Assert.assertThat(subset,
					Matchers.everyItem(Matchers.isIn(testList)));
		}

		@Test
		public void testEmpty() {
			RandomSubset.doRandomSubsetTest(UtilsTest.EMPTY_LIST, 0);
		}

		@Test(expected = IllegalArgumentException.class)
		public void testEmptyException() {
			RandomSubset.doRandomSubsetTest(UtilsTest.EMPTY_LIST, 10);
		}

		@Test
		public void testOneItem() {
			RandomSubset.doRandomSubsetTest(UtilsTest.ONE_ITEM_LIST, 1);
		}

		@Test
		public void testNormal() {
			RandomSubset.doRandomSubsetTest(UtilsTest.TEST_LIST, 4);
		}

		@Test
		public void testMax() {
			RandomSubset.doRandomSubsetTest(UtilsTest.TEST_LIST, 10);
		}
	}

	@RunWith(JUnit4.class)
	public static class RandomItem {
		private static void doRandomItemTest(List<Integer> testList) {
			// Chosen item must be in the original list
			Assert.assertThat(Utils.randomItem(testList),
					Matchers.isIn(testList));
		}

		@Test
		public void testNormal() {
			RandomItem.doRandomItemTest(UtilsTest.TEST_LIST);
		}

		@Test(expected = IllegalArgumentException.class)
		public void testEmpty() {
			RandomItem.doRandomItemTest(UtilsTest.EMPTY_LIST);
		}

		@Test
		public void testOneItem() {
			RandomItem.doRandomItemTest(UtilsTest.ONE_ITEM_LIST);
		}
	}

	@RunWith(JUnit4.class)
	public static class RandomItemWithInvalid {
		private static void doRandomItemTest(List<Integer> testList,
				Integer invalidItem) {
			Integer chosenItem = Utils.randomItem(testList, invalidItem);

			// Random item must be in original list and not be invalid
			Assert.assertThat(chosenItem, Matchers.isIn(testList));
			Assert.assertThat(chosenItem, Matchers.not(invalidItem));
		}

		@Test
		public void testNormal() {
			RandomItemWithInvalid.doRandomItemTest(UtilsTest.TEST_LIST,
					UtilsTest.TEST_LIST.get(2));
		}

		@Test(expected = IllegalArgumentException.class)
		public void testOneException() {
			RandomItemWithInvalid.doRandomItemTest(UtilsTest.ONE_ITEM_LIST,
					UtilsTest.ONE_ITEM_LIST.get(0));
		}

		@Test
		public void testOne() {
			RandomItemWithInvalid.doRandomItemTest(UtilsTest.ONE_ITEM_LIST, 60);
		}

		@Test(expected = IllegalArgumentException.class)
		public void testEmptyException() {
			RandomItemWithInvalid.doRandomItemTest(UtilsTest.EMPTY_LIST, 60);
		}
	}
}
