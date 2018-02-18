package instrumentation.goodlock;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GoodLockTaskTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		GoodLockTask aTask = new GoodLockTask(1, 2, 3, 4, 5);
		GoodLockTask task1 = new GoodLockTask(6, 2, 3, 4, 5);
		GoodLockTask task2 = new GoodLockTask(1, 6, 3, 4, 5);
		GoodLockTask task3 = new GoodLockTask(1, 2, 6, 4, 5);
		GoodLockTask task4 = new GoodLockTask(1, 2, 3, 6, 5);
		GoodLockTask task5 = new GoodLockTask(1, 2, 3, 4, 6);
		GoodLockTask sameTask = new GoodLockTask(1, 2, 3, 4, 5);

		Assert.assertTrue(aTask.hashCode() == aTask.hashCode());
		Assert.assertTrue(aTask.hashCode() == sameTask.hashCode());

		Assert.assertFalse(aTask.hashCode() == task1.hashCode());
		Assert.assertFalse(aTask.hashCode() == task2.hashCode());
		Assert.assertFalse(aTask.hashCode() == task3.hashCode());
		Assert.assertFalse(aTask.hashCode() == task4.hashCode());
		Assert.assertFalse(aTask.hashCode() == task5.hashCode());
	}

	@Test
	public void testEquals() {
		GoodLockTask aTask = new GoodLockTask(1, 2, 3, 4, 5);
		GoodLockTask task1 = new GoodLockTask(6, 2, 3, 4, 5);
		GoodLockTask task2 = new GoodLockTask(1, 6, 3, 4, 5);
		GoodLockTask task3 = new GoodLockTask(1, 2, 6, 4, 5);
		GoodLockTask task4 = new GoodLockTask(1, 2, 3, 6, 5);
		GoodLockTask task5 = new GoodLockTask(1, 2, 3, 4, 6);
		GoodLockTask sameTask = new GoodLockTask(1, 2, 3, 4, 5);

		Assert.assertTrue(aTask.equals(aTask));

		Assert.assertFalse(aTask.equals(null));
		Assert.assertFalse(aTask.equals(new Object()));

		Assert.assertFalse(aTask.equals(task1));
		Assert.assertFalse(aTask.equals(task2));
		Assert.assertFalse(aTask.equals(task3));
		Assert.assertFalse(aTask.equals(task4));
		Assert.assertFalse(aTask.equals(task5));

		Assert.assertTrue(aTask.equals(sameTask));
	}
	
	@Test
	public void testToString() {
		GoodLockTask aTask = new GoodLockTask(1, 2, 3, 4, 5);

		Assert.assertTrue(aTask.toString().contains("1"));
		Assert.assertTrue(aTask.toString().contains("2"));
		Assert.assertTrue(aTask.toString().contains("3"));
		Assert.assertTrue(aTask.toString().contains("4"));
		Assert.assertTrue(aTask.toString().contains("5"));
	}
}
