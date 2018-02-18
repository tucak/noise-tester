package instrumentation.dupairs;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class DUPairsInstrumentationTest {
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
		DUPairsInstrumentation.reset();
	}

	@Test
	public void testWrite() {
		DUPairsInstrumentation dpi = new DUPairsInstrumentation(mock);
		dpi.visitFieldInsn(Opcodes.PUTFIELD, "a", "b", "c");
		Assert.assertEquals(DUPairsMetric.class.getName().replace('.', '/'), target);
		target = null;
		dpi.visitFieldInsn(Opcodes.PUTSTATIC, "a", "b", "c");
		Assert.assertEquals(DUPairsMetric.class.getName().replace('.', '/'), target);
	}

	@Test
	public void testRead() {
		DUPairsInstrumentation dpi = new DUPairsInstrumentation(mock);
		dpi.visitFieldInsn(Opcodes.GETFIELD, "a", "b", "c");
		Assert.assertEquals(DUPairsMetric.class.getName().replace('.', '/'), target);
		target = null;
		dpi.visitFieldInsn(Opcodes.GETSTATIC, "a", "b", "c");
		Assert.assertEquals(DUPairsMetric.class.getName().replace('.', '/'), target);
	}

}
