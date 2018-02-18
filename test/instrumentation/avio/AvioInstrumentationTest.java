package instrumentation.avio;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AvioInstrumentationTest {
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
		AvioInstrumentation.reset();
	}

	@Test
	public void testWrite() {
		AvioInstrumentation ai = new AvioInstrumentation(mock);
		ai.visitFieldInsn(Opcodes.PUTFIELD, "a", "b", "c");
		Assert.assertEquals(AvioMetric.class.getName().replace('.', '/'), target);
		target = null;
		ai.visitFieldInsn(Opcodes.PUTSTATIC, "a", "b", "c");
		Assert.assertEquals(AvioMetric.class.getName().replace('.', '/'), target);
	}

	@Test
	public void testRead() {
		AvioInstrumentation ai = new AvioInstrumentation(mock);
		ai.visitFieldInsn(Opcodes.GETFIELD, "a", "b", "c");
		Assert.assertEquals(AvioMetric.class.getName().replace('.', '/'), target);
		target = null;
		ai.visitFieldInsn(Opcodes.GETSTATIC, "a", "b", "c");
		Assert.assertEquals(AvioMetric.class.getName().replace('.', '/'), target);
	}
}
