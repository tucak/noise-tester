package instrumentation.sync;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SyncTaskTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		SyncTask aTask = new SyncTask(0, SyncEvent.VISITED);
		SyncTask anotherTask = new SyncTask(1, SyncEvent.VISITED);
		SyncTask blockingTask = new SyncTask(0, SyncEvent.BLOCKING);
		SyncTask blockedTask = new SyncTask(0, SyncEvent.BLOCKED);

		SyncTask sameTask = new SyncTask(0, SyncEvent.VISITED);

		SyncTask nullTask = new SyncTask(0, null);

		Assert.assertTrue(aTask.hashCode() == sameTask.hashCode());

		Assert.assertFalse(aTask.hashCode() == anotherTask.hashCode());
		Assert.assertFalse(aTask.hashCode() == blockingTask.hashCode());
		Assert.assertFalse(aTask.hashCode() == blockedTask.hashCode());
		Assert.assertFalse(aTask.hashCode() == nullTask.hashCode());
	}

	@Test
	public void testEqualsObject() {
		SyncTask aTask = new SyncTask(0, SyncEvent.VISITED);
		SyncTask anotherTask = new SyncTask(1, SyncEvent.VISITED);
		SyncTask blockingTask = new SyncTask(0, SyncEvent.BLOCKING);
		SyncTask blockedTask = new SyncTask(0, SyncEvent.BLOCKED);

		SyncTask sameTask = new SyncTask(0, SyncEvent.VISITED);

		SyncTask nullTask = new SyncTask(0, null);

		Assert.assertTrue(aTask.equals(aTask));

		Assert.assertFalse(aTask.equals(null));
		Assert.assertFalse(aTask.equals(new Object()));

		Assert.assertTrue(aTask.equals(sameTask));

		Assert.assertFalse(aTask.equals(anotherTask));
		Assert.assertFalse(aTask.equals(blockingTask));
		Assert.assertFalse(aTask.equals(blockedTask));
		Assert.assertFalse(aTask.equals(nullTask));
	}
	
	@Test
	public void testToString()	 {
		SyncTask aTask = new SyncTask(0, SyncEvent.VISITED);
		
		Assert.assertTrue(aTask.toString().contains("0"));
		Assert.assertTrue(aTask.toString().contains("VISITED"));
	}

}
