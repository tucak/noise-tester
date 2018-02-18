package instrumentation.dupairs;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DUParisMetricTests {

	@Before
	public void setUp() throws Exception {
		DUPairsMetric.lastWritePosition.clear();
		DUPairsMetric.lastWriteThread.clear();
		DUPairsMetric.tasks.clear();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testReadNothing() {
		DUPairsMetric.readAccess(0, 0);

		Assert.assertTrue(DUPairsMetric.tasks.isEmpty());
	}

	@Test
	public void testWriteNothing() {
		DUPairsMetric.writeAccess(0, 1);

		Assert.assertTrue(DUPairsMetric.tasks.isEmpty());
	}

	@Test
	public void testWriteSameReadNothing() {
		DUPairsMetric.writeAccess(0, 1);
		DUPairsMetric.readAccess(0, 1);

		Assert.assertTrue(DUPairsMetric.tasks.isEmpty());
	}

	@Test
	public void testWriteOtherRead() throws Exception {
		DUPairsMetric.writeAccess(0, 1);
		Thread t = new Thread(() -> DUPairsMetric.readAccess(1, 1));
		t.start();
		t.join();

		Assert.assertEquals(1, DUPairsMetric.tasks.size());

		DUPairsTask task = DUPairsMetric.tasks.peek();

		Assert.assertEquals(1, task.v);
		Assert.assertEquals(0, task.p1);
		Assert.assertEquals(1, task.p2);
	}
}
