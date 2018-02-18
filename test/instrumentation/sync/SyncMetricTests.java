package instrumentation.sync;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SyncMetricTests {

	@Before
	public void setUp() throws Exception {
		SyncMetric.locks.clear();
		SyncMetric.tasks.clear();
	}

	@After
	public void tearDown() throws Exception {
		SyncMetric.locks.clear();
		SyncMetric.tasks.clear();
	}

	@Test
	public void testVisitedEnter() {
		Object o = new Object();
		SyncMetric.beforeMonitorEnter(o, 0);

		Assert.assertEquals(new SyncTask(0, SyncEvent.VISITED), SyncMetric.tasks.peek());
	}

	@Test
	public void testViditedExit() {
		Object o = new Object();
		SyncMetric.beforeMonitorExit(o, 0);

		Assert.assertEquals(new SyncTask(0, SyncEvent.VISITED), SyncMetric.tasks.peek());
	}

	@Test
	public void testBlockedEnter() {
		Object o = new Object();
		SyncMetric.beforeMonitorEnter(o, 0);
		SyncMetric.afterMonitorEnter(o, 1);
		SyncMetric.beforeMonitorEnter(o, 2);

		SyncTask blocked = new SyncTask(2, SyncEvent.BLOCKED);
		SyncTask blocking = new SyncTask(1, SyncEvent.BLOCKING);

		Assert.assertTrue(SyncMetric.tasks.contains(blocked));
		Assert.assertTrue(SyncMetric.tasks.contains(blocking));
	}

	@Test
	public void testMonitorNotBlockingAfterRelease() {
		Object o = new Object();

		SyncMetric.beforeMonitorEnter(o, 0);
		SyncMetric.afterMonitorEnter(o, 1);
		SyncMetric.beforeMonitorExit(o, 2);
		SyncMetric.beforeMonitorEnter(o, 3);

		Assert.assertTrue(SyncMetric.tasks.stream().allMatch(task -> task.type != SyncEvent.BLOCKING));
		Assert.assertTrue(SyncMetric.tasks.stream().allMatch(task -> task.type != SyncEvent.BLOCKED));
	}
}
