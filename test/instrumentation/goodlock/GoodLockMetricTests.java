package instrumentation.goodlock;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GoodLockMetricTests {

	@Before
	public void setUp() throws Exception {
		GoodLockMetric.clear();
	}

	@After
	public void tearDown() throws Exception {
		GoodLockMetric.clear();
	}

	@Test
	public void testNothing() {
		GoodLockMetric.afterMonitorEnter(new Object(), 0);

		Assert.assertTrue(GoodLockMetric.tasks.isEmpty());
	}
	
	@Test
	public void testSingleLockDoesNothing() {
		Object o1 = new Object();
		
		GoodLockMetric.afterMonitorEnter(o1, 0);
		GoodLockMetric.beforeMonitorExit(o1, 1);
		
		Assert.assertTrue(GoodLockMetric.tasks.isEmpty());
	}
	
	@Test
	public void testLockingWhileLockedGeneratesTask() {
		Object o1 = new Object();
		Object o2 = new Object();
		
		GoodLockMetric.afterMonitorEnter(o1, 0);
		GoodLockMetric.afterMonitorEnter(o2, 1);
		
		Assert.assertEquals(1, GoodLockMetric.tasks.size());
	}
	
	@Test
	public void testNewTaskHasTheLastPlace() {
		Object o1 = new Object();
		Object o2 = new Object();
		Object o3 = new Object();
		
		GoodLockMetric.afterMonitorEnter(o1, 0);
		GoodLockMetric.afterMonitorEnter(o2, 1);
		GoodLockMetric.afterMonitorEnter(o3, 2);
		
		Assert.assertEquals(2, GoodLockMetric.tasks.size());
		
		GoodLockMetric.tasks.remove();

		Assert.assertEquals(1, GoodLockMetric.tasks.peek().pl1);
		Assert.assertEquals(2, GoodLockMetric.tasks.peek().pl2);
	}

}
