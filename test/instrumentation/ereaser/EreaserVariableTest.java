package instrumentation.ereaser;

import java.util.Arrays;
import java.util.Set;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class EreaserVariableTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testVirgin() {
		EreaserVariable var = new EreaserVariable();

		Assert.assertEquals(EreaserState.VIRGIN, var.state);
		Assert.assertTrue(var.lockset.isEmpty());
	}

	@Test
	public void testExclusive() {
		EreaserVariable var = new EreaserVariable();

		var.read(0, null);
		var.read(0, null);

		Assert.assertEquals(EreaserState.EXCLUSIVE, var.state);
		Assert.assertTrue(var.lockset.isEmpty());
	}

	@Test
	public void testExclusiveModified() {
		EreaserVariable var = new EreaserVariable();

		var.write(0, null);

		Assert.assertEquals(EreaserState.EXCLUSIVE_MODIFIED, var.state);
		Assert.assertTrue(var.lockset.isEmpty());
	}

	@Test
	public void testShared() {
		EreaserVariable var = new EreaserVariable();

		var.read(0, Arrays.asList(1, 2));
		var.read(1, Arrays.asList(1, 3));
		var.read(0, Arrays.asList(1, 2, 3));

		Assert.assertEquals(EreaserState.SHARED, var.state);
		Assert.assertEquals(1, var.lockset.size());
		Assert.assertTrue(var.lockset.contains(1));
	}

	@Test
	public void testSharedModifiedWrite() {
		EreaserVariable var = new EreaserVariable();

		var.write(0, null);
		var.write(0, null);
		var.write(1, null);

		Assert.assertEquals(EreaserState.SHARED_MODIFIED, var.state);
	}

	@Test
	public void testSharedModifiedRead() {
		EreaserVariable var = new EreaserVariable();

		var.read(0, null);
		var.write(1, null);
		var.write(1, null);
		var.read(0, null);

		Assert.assertEquals(EreaserState.SHARED_MODIFIED, var.state);
	}

	@Test
	public void testExclusiveModifiedReadWrite() {
		EreaserVariable var = new EreaserVariable();

		var.read(0, null);
		var.write(0, null);

		Assert.assertEquals(EreaserState.EXCLUSIVE_MODIFIED, var.state);
	}

	@Test
	public void testSharedToSharedModified() {
		EreaserVariable var = new EreaserVariable();

		var.read(0, null);
		var.read(1, null);
		var.write(0, null);

		Assert.assertEquals(EreaserState.SHARED_MODIFIED, var.state);
	}
	
	@Test
	public void testExclusiveModifiedReadWriteRead() {
		EreaserVariable var = new EreaserVariable();

		var.read(0, null);
		var.write(0, null);
		var.read(0, null);

		Assert.assertEquals(EreaserState.EXCLUSIVE_MODIFIED, var.state);
	}
	
	@Test
	public void testExclusiveModifiedReadWriteAnotherRead() {
		EreaserVariable var = new EreaserVariable();

		var.read(0, null);
		var.write(0, null);
		var.read(1, null);

		Assert.assertEquals(EreaserState.SHARED_MODIFIED, var.state);
	}
	
	@Test
	public void testEmptyIntersect() {
		EreaserVariable var = new EreaserVariable();
		EreaserVariable var2 = new EreaserVariable();
		Set<Integer> empty = var.lockset;

		var.read(0, empty);
		var.read(1, empty);

		var2.read(0, Arrays.asList(1, 2, 3, 4, 5));
		var2.read(1, Arrays.asList());

		Assert.assertEquals(empty, var.lockset);
		Assert.assertEquals(empty, var2.lockset);
	}
	
	@Test
	public void testEmptyIntersectWrite() {
		EreaserVariable var = new EreaserVariable();
		EreaserVariable var2 = new EreaserVariable();
		Set<Integer> empty = var.lockset;

		var.write(0, empty);
		var.write(1, empty);

		var2.write(0, Arrays.asList(1, 2, 3, 4, 5));
		var2.write(1, Arrays.asList());

		Assert.assertEquals(empty, var.lockset);
		Assert.assertEquals(empty, var2.lockset);
	}

	@Test
	public void testNoLockset() {
		EreaserVariable var = new EreaserVariable();

		Assert.assertNull(var.getLockset());
	}

	@Test
	public void testSharedLockset() {
		EreaserVariable var = new EreaserVariable();

		var.read(0, Arrays.asList(1, 2, 3));
		var.read(1, Arrays.asList(1, 2, 3));

		Assert.assertNotNull(var.getLockset());
	}

	@Test
	public void testSharedModifiedLockset() {
		EreaserVariable var = new EreaserVariable();

		var.read(0, Arrays.asList(1, 2, 3));
		var.write(1, Arrays.asList(1, 2, 3));

		Assert.assertNotNull(var.getLockset());
	}
}
