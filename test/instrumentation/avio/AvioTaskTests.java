package instrumentation.avio;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AvioTaskTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		AvioTask task1 = new AvioTask(0, 1, 2, 3, 4);
		AvioTask task2 = new AvioTask(5, 1, 2, 3, 4);
		AvioTask task3 = new AvioTask(0, 5, 2, 3, 4);
		AvioTask task4 = new AvioTask(0, 1, 5, 3, 4);
		AvioTask task5 = new AvioTask(0, 1, 2, 5, 4);
		AvioTask task6 = new AvioTask(0, 1, 2, 3, 5);
		AvioTask task7 = new AvioTask(0, 1, 2, 3, 4);

		Assert.assertTrue(task1.hashCode() == task1.hashCode());

		Assert.assertFalse(task1.hashCode() == task2.hashCode());
		Assert.assertFalse(task1.hashCode() == task3.hashCode());
		Assert.assertFalse(task1.hashCode() == task4.hashCode());
		Assert.assertFalse(task1.hashCode() == task5.hashCode());
		Assert.assertFalse(task1.hashCode() == task6.hashCode());

		Assert.assertTrue(task1.hashCode() == task7.hashCode());
	}

	@Test
	public void testEquals() {
		AvioTask task1 = new AvioTask(0, 1, 2, 3, 4);
		AvioTask task2 = new AvioTask(5, 1, 2, 3, 4);
		AvioTask task3 = new AvioTask(0, 5, 2, 3, 4);
		AvioTask task4 = new AvioTask(0, 1, 5, 3, 4);
		AvioTask task5 = new AvioTask(0, 1, 2, 5, 4);
		AvioTask task6 = new AvioTask(0, 1, 2, 3, 5);
		AvioTask task7 = new AvioTask(0, 1, 2, 3, 4);

		Assert.assertFalse(task1.equals(null));
		Assert.assertFalse(task1.equals(new Object()));

		Assert.assertTrue(task1.equals(task1));

		Assert.assertFalse(task1.equals(task2));
		Assert.assertFalse(task1.equals(task3));
		Assert.assertFalse(task1.equals(task4));
		Assert.assertFalse(task1.equals(task5));
		Assert.assertFalse(task1.equals(task6));

		Assert.assertTrue(task1.equals(task7));
	}

	@Test
	public void testToString() {
		AvioTask aTask = new AvioTask(0, 1, 2, 3, 4);

		Assert.assertTrue(aTask.toString().contains("0"));
		Assert.assertTrue(aTask.toString().contains("1"));
		Assert.assertTrue(aTask.toString().contains("2"));
		Assert.assertTrue(aTask.toString().contains("3"));
		Assert.assertTrue(aTask.toString().contains("4"));
	}

}
