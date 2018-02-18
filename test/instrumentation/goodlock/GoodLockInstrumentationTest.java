package instrumentation.goodlock;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class GoodLockInstrumentationTest {
	private MethodVisitor mock;
	private String target;

	@Before
	public void setUp() throws Exception {
		mock = new MethodVisitor(Opcodes.ASM5) {

			@Override
			public void visitMethodInsn(int arg0, String arg1, String arg2, String arg3, boolean arg4) {
				target = arg1;
			}
		};
		target = null;
	}

	@After
	public void tearDown() throws Exception {
		GoodLockInstrumentation.reset();
	}

	@Test
	public void testMotitor() {
		GoodLockInstrumentation gli = new GoodLockInstrumentation(mock);
		gli.visitInsn(Opcodes.NOP);
		gli.visitInsn(Opcodes.MONITORENTER);
		Assert.assertEquals(GoodLockMetric.class.getName().replace('.', '/'), target);
		target = null;
		gli.visitInsn(Opcodes.MONITOREXIT);
		Assert.assertEquals(GoodLockMetric.class.getName().replace('.', '/'), target);
	}
}
