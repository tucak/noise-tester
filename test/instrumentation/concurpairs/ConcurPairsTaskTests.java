package instrumentation.concurpairs;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ConcurPairsTaskTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHash() {
		ConcurPairsTask aTask = new ConcurPairsTask(0, 1, false);
		ConcurPairsTask anotherTask = new ConcurPairsTask(1, 0, false);
		ConcurPairsTask aSwitchedTask = new ConcurPairsTask(0, 1, true);
		ConcurPairsTask anotherSwitchedTask = new ConcurPairsTask(1, 0, true);

		ConcurPairsTask sameTask = new ConcurPairsTask(0, 1, false);

		Assert.assertFalse(aTask.hashCode() == anotherTask.hashCode());
		Assert.assertFalse(aTask.hashCode() == aSwitchedTask.hashCode());
		Assert.assertFalse(aTask.hashCode() == anotherSwitchedTask.hashCode());

		Assert.assertTrue(aTask.hashCode() == sameTask.hashCode());
	}

	@Test
	public void testEquals() {
		ConcurPairsTask aTask = new ConcurPairsTask(0, 1, false);
		ConcurPairsTask anotherTask = new ConcurPairsTask(1, 0, false);
		ConcurPairsTask aSwitchedTask = new ConcurPairsTask(0, 1, true);
		ConcurPairsTask anotherSwitchedTask = new ConcurPairsTask(1, 0, true);
		
		ConcurPairsTask almostSameTask = new ConcurPairsTask(0, 2, false);

		ConcurPairsTask sameTask = new ConcurPairsTask(0, 1, false);

		Assert.assertFalse(aTask.equals(null));
		Assert.assertFalse(aTask.equals(new Object()));
		
		Assert.assertTrue(aTask.equals(aTask));
		
		Assert.assertFalse(aTask.equals(anotherTask));
		Assert.assertFalse(aTask.equals(aSwitchedTask));
		Assert.assertFalse(aTask.equals(anotherSwitchedTask));
		Assert.assertFalse(aTask.equals(almostSameTask));

		Assert.assertTrue(aTask.equals(sameTask));
	}
	
	@Test
	public void testToString() {
		ConcurPairsTask aTask = new ConcurPairsTask(0, 1, false);

		Assert.assertTrue(aTask.toString().contains("0"));
		Assert.assertTrue(aTask.toString().contains("1"));
		Assert.assertTrue(aTask.toString().toLowerCase().contains("false"));
	}
}
