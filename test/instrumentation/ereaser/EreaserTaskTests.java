package instrumentation.ereaser;

import java.util.Arrays;
import java.util.HashSet;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EreaserTaskTests {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testHashCode() {
		EreaserTask anEreaserTask = new EreaserTask(0, 1, EreaserState.VIRGIN, new HashSet<>(), 2);
		EreaserTask task1 = new EreaserTask(1, 1, EreaserState.VIRGIN, null, 2);
		EreaserTask task2 = new EreaserTask(0, 2, EreaserState.VIRGIN, null, 2);
		EreaserTask task3 = new EreaserTask(0, 1, EreaserState.EXCLUSIVE, null, 2);
		EreaserTask task4 = new EreaserTask(0, 1, EreaserState.VIRGIN, new HashSet<>(Arrays.asList(1, 2, 3)), 2);
		EreaserTask task5 = new EreaserTask(0, 1, null, null, 2);
		EreaserTask task6 = new EreaserTask(0, 1, EreaserState.VIRGIN, new HashSet<>(), 2);

		Assert.assertTrue(anEreaserTask.hashCode() == anEreaserTask.hashCode());
		Assert.assertFalse(anEreaserTask.hashCode() == task1.hashCode());
		Assert.assertFalse(anEreaserTask.hashCode() == task2.hashCode());
		Assert.assertFalse(anEreaserTask.hashCode() == task3.hashCode());
		Assert.assertFalse(anEreaserTask.hashCode() == task4.hashCode());
		Assert.assertFalse(anEreaserTask.hashCode() == task5.hashCode());
		Assert.assertTrue(anEreaserTask.hashCode() == task6.hashCode());
	}

	@Test
	public void testEquals() {
		EreaserTask anEreaserTask = new EreaserTask(0, 1, EreaserState.VIRGIN, new HashSet<>(), 2);
		EreaserTask task1 = new EreaserTask(1, 1, EreaserState.VIRGIN, null, 2);
		EreaserTask task2 = new EreaserTask(0, 2, EreaserState.VIRGIN, null, 2);
		EreaserTask task3 = new EreaserTask(0, 1, EreaserState.EXCLUSIVE, null, 2);
		EreaserTask task4 = new EreaserTask(0, 1, EreaserState.VIRGIN, new HashSet<>(Arrays.asList(1, 2, 3)), 2);
		EreaserTask task5 = new EreaserTask(0, 1, null, null, 2);
		EreaserTask task6 = new EreaserTask(0, 1, EreaserState.VIRGIN, new HashSet<>(), 2);
		EreaserTask task7 = new EreaserTask(0, 1, null, null, 1);
		EreaserTask task8 = new EreaserTask(0, 2, null, null, 1);

		Assert.assertTrue(anEreaserTask.equals(anEreaserTask));

		Assert.assertFalse(anEreaserTask.equals(null));
		Assert.assertFalse(anEreaserTask.equals(new Object()));

		Assert.assertFalse(anEreaserTask.equals(task1));
		Assert.assertFalse(anEreaserTask.equals(task2));
		Assert.assertFalse(anEreaserTask.equals(task3));
		Assert.assertFalse(anEreaserTask.equals(task4));
		Assert.assertFalse(anEreaserTask.equals(task5));

		Assert.assertFalse(task3.equals(anEreaserTask));
		Assert.assertFalse(task3.equals(task5));
		Assert.assertFalse(task1.equals(task2));
		Assert.assertFalse(task5.equals(task7));
		Assert.assertFalse(task7.equals(task8));

		Assert.assertTrue(anEreaserTask.equals(task6));
	}

	@Test
	public void testToString() {
		EreaserTask aTask = new EreaserTask(0, 1, EreaserState.VIRGIN, null, 4);
				
		Assert.assertTrue(aTask.toString().contains("0"));
		Assert.assertTrue(aTask.toString().contains("1"));
		Assert.assertTrue(aTask.toString().contains("VIRGIN"));
		Assert.assertTrue(aTask.toString().contains("null"));
		Assert.assertTrue(aTask.toString().contains("4"));
	}

}
