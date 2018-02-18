package instrumentation.dupairs;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DUParisTaskTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		DUPairsTask task1 = new DUPairsTask(0, 1, 2, 3, 4);
		DUPairsTask task2 = new DUPairsTask(1, 1, 2, 3, 4);
		DUPairsTask task3 = new DUPairsTask(0, 2, 3, 4, 5);
		DUPairsTask task4 = new DUPairsTask(0, 1, 3, 4, 5);
		DUPairsTask task5 = new DUPairsTask(0, 1, 2, 4, 5);
		DUPairsTask task6 = new DUPairsTask(0, 1, 2, 3, 5);
		DUPairsTask task7 = new DUPairsTask(0, 1, 2, 3, 4);

		Assert.assertFalse(task1.hashCode() == task2.hashCode());
		Assert.assertFalse(task1.hashCode() == task3.hashCode());
		Assert.assertFalse(task1.hashCode() == task4.hashCode());
		Assert.assertFalse(task1.hashCode() == task5.hashCode());
		Assert.assertFalse(task1.hashCode() == task6.hashCode());

		Assert.assertTrue(task1.hashCode() == task7.hashCode());
	}

	@Test
	public void testEquals() {
		DUPairsTask task1 = new DUPairsTask(0, 1, 2, 3, 4);
		DUPairsTask task2 = new DUPairsTask(1, 1, 2, 3, 4);
		DUPairsTask task3 = new DUPairsTask(0, 2, 3, 4, 5);
		DUPairsTask task4 = new DUPairsTask(0, 1, 3, 4, 5);
		DUPairsTask task5 = new DUPairsTask(0, 1, 2, 4, 5);
		DUPairsTask task6 = new DUPairsTask(0, 1, 2, 3, 5);
		DUPairsTask task7 = new DUPairsTask(0, 1, 2, 3, 4);

		Assert.assertTrue(task1.equals(task1));

		Assert.assertFalse(task1.equals(null));
		Assert.assertFalse(task1.equals(new Object()));

		Assert.assertFalse(task1.equals(task2));
		Assert.assertFalse(task1.equals(task3));
		Assert.assertFalse(task1.equals(task4));
		Assert.assertFalse(task1.equals(task5));
		Assert.assertFalse(task1.equals(task6));

		Assert.assertTrue(task1.equals(task7));
	}

	@Test
	public void testToString() {
		DUPairsTask aTask = new DUPairsTask(0, 1, 2, 3, 4);

		Assert.assertTrue(aTask.toString().contains("0"));
		Assert.assertTrue(aTask.toString().contains("1"));
		Assert.assertTrue(aTask.toString().contains("2"));
		Assert.assertTrue(aTask.toString().contains("3"));
		Assert.assertTrue(aTask.toString().contains("4"));
	}

}
