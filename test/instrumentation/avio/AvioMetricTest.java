package instrumentation.avio;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AvioMetricTest {

	@Before
	public void setUp() throws Exception {
		AvioMetric.clear();
	}

	@After
	public void tearDown() throws Exception {
		AvioMetric.clear();
	}

	@Test
	public void testNothing() {
		AvioMetric.readAccess(0, 0);

		Assert.assertTrue(AvioMetric.tasks.isEmpty());
	}

	@Test
	public void testSameWrite() {
		AvioMetric.writeAccess(0, 0);
		AvioMetric.readAccess(0, 0);

		Assert.assertTrue(AvioMetric.tasks.isEmpty());
	}

	@Test
	public void testSameRead() {
		AvioMetric.writeAccess(0, 0);
		AvioMetric.writeAccess(0, 0);

		Assert.assertTrue(AvioMetric.tasks.isEmpty());
	}

	@Test
	public void testWriteRead() throws Exception {
		Thread aThread = new Thread(() -> AvioMetric.writeAccess(0, 0));
		aThread.start();
		aThread.join();
		AvioMetric.readAccess(0, 0);

		Assert.assertTrue(AvioMetric.tasks.isEmpty());
	}

	public void testReadOtherReadRead() throws Exception {
		AvioMetric.readAccess(0, 0);
		Thread aThread = new Thread(() -> AvioMetric.readAccess(0, 0));
		aThread.start();
		aThread.join();
		AvioMetric.readAccess(0, 0);

		Assert.assertTrue(AvioMetric.tasks.isEmpty());
	}

	@Test
	public void testReadOtherWriteRead() throws Exception {
		AvioMetric.readAccess(0, 0);
		Thread aThread = new Thread(() -> AvioMetric.writeAccess(0, 0));
		aThread.start();
		aThread.join();
		AvioMetric.readAccess(0, 0);

		Assert.assertEquals(1, AvioMetric.tasks.size());
	}

	@Test
	public void testWriteOtherWriteRead() throws Exception {
		AvioMetric.writeAccess(0, 0);
		Thread aThread = new Thread(() -> AvioMetric.writeAccess(0, 0));
		aThread.start();
		aThread.join();
		AvioMetric.readAccess(0, 0);

		Assert.assertEquals(1, AvioMetric.tasks.size());
	}

	@Test
	public void testReadOtherWriteWrite() throws Exception {
		AvioMetric.readAccess(0, 0);
		Thread aThread = new Thread(() -> AvioMetric.writeAccess(0, 0));
		aThread.start();
		aThread.join();
		AvioMetric.writeAccess(0, 0);

		Assert.assertEquals(1, AvioMetric.tasks.size());
	}

	@Test
	public void testWriteOtherReadWrite() throws Exception {
		AvioMetric.writeAccess(0, 0);
		Thread aThread = new Thread(() -> AvioMetric.readAccess(0, 0));
		aThread.start();
		aThread.join();
		AvioMetric.writeAccess(0, 0);

		Assert.assertEquals(1, AvioMetric.tasks.size());
	}

}
