package instrumentation.concurpairs;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConcurpairsMetricTests {

	@Before
	public void setUp() throws Exception {
		ConcurPairsMetric.init(10);
	}

	@After
	public void tearDown() throws Exception {
		ConcurPairsMetric.q.clear();
	}

	@Test
	public void testTaskWithoutSwitch() {
		ConcurPairsMetric.cover(0);
		ConcurPairsMetric.cover(1);

		Assert.assertEquals(new ConcurPairsTask(0, 1, false), ConcurPairsMetric.q.peek());
	}

	@Test
	public void testTaskWithSwitch() throws Exception {
		ConcurPairsMetric.cover(0);
		Thread t = new Thread(() -> ConcurPairsMetric.cover(0));
		t.start();
		t.join();
		ConcurPairsMetric.cover(1);

		Assert.assertEquals(new ConcurPairsTask(0, 1, true), ConcurPairsMetric.q.peek());
	}

	@Test
	public void testTwoTaskWithoutSwitch() throws Exception {
		ConcurPairsMetric.cover(0);
		Thread t = new Thread(() -> {
			ConcurPairsMetric.cover(2);
			ConcurPairsMetric.cover(3);
		});
		t.start();
		t.join();
		ConcurPairsMetric.cover(1);

		Assert.assertEquals(2, ConcurPairsMetric.q.size());
		Assert.assertTrue(ConcurPairsMetric.q.stream().allMatch(task -> !task.switched));
	}

	@Test
	public void testTwoTaskWithSwitch() throws Exception {
		ConcurPairsMetric.cover(0);
		Thread t = new Thread(() -> {
			ConcurPairsMetric.cover(0);
			ConcurPairsMetric.cover(3);
		});
		t.start();
		t.join();
		ConcurPairsMetric.cover(1);

		Assert.assertEquals(2, ConcurPairsMetric.q.size());
		Assert.assertTrue(ConcurPairsMetric.q.stream().anyMatch(task -> !task.switched));
		Assert.assertTrue(ConcurPairsMetric.q.stream().anyMatch(task -> task.switched));
	}

}
