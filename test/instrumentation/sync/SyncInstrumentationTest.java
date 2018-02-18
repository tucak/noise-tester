package instrumentation.sync;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class SyncInstrumentationTest {
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
	}

	@Test
	public void testMotitor() {
		SyncInstrumentation si = new SyncInstrumentation(mock);
		si.visitInsn(Opcodes.NOP);
		si.visitInsn(Opcodes.MONITORENTER);
		Assert.assertEquals(SyncMetric.class.getName().replace('.', '/'), target);
		target = null;
		si.visitInsn(Opcodes.MONITOREXIT);
		Assert.assertEquals(SyncMetric.class.getName().replace('.', '/'), target);
	}
}
