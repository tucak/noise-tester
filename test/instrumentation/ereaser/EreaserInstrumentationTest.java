package instrumentation.ereaser;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class EreaserInstrumentationTest {
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
		EreaserInstrumentation.reset();
	}

	@Test
	public void testWrite() {
		EreaserInstrumentation ei = new EreaserInstrumentation(mock);
		ei.visitFieldInsn(Opcodes.PUTFIELD, "a", "b", "c");
		Assert.assertEquals(EreaserMetric.class.getName().replace('.', '/'), target);
		target = null;
		ei.visitFieldInsn(Opcodes.PUTSTATIC, "a", "b", "c");
		Assert.assertEquals(EreaserMetric.class.getName().replace('.', '/'), target);
	}

	@Test
	public void testRead() {
		EreaserInstrumentation ei = new EreaserInstrumentation(mock);
		ei.visitFieldInsn(Opcodes.GETFIELD, "a", "b", "c");
		Assert.assertEquals(EreaserMetric.class.getName().replace('.', '/'), target);
		target = null;
		ei.visitFieldInsn(Opcodes.GETSTATIC, "a", "b", "c");
		Assert.assertEquals(EreaserMetric.class.getName().replace('.', '/'), target);
	}

	@Test
	public void testMotitor() {
		EreaserInstrumentation ei = new EreaserInstrumentation(mock);
		ei.visitInsn(Opcodes.NOP);
		ei.visitInsn(Opcodes.MONITORENTER);
		Assert.assertEquals(EreaserMetric.class.getName().replace('.', '/'), target);
		target = null;
		ei.visitInsn(Opcodes.MONITOREXIT);
		Assert.assertEquals(EreaserMetric.class.getName().replace('.', '/'), target);
	}
}
