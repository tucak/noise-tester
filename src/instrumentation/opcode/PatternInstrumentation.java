package instrumentation.opcode;

import java.util.HashMap;
import java.util.Map;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class PatternInstrumentation extends Instrumentation {

	public Map<String, Boolean> seen = new HashMap<>();

	public PatternInstrumentation(MethodVisitor mv) {
		super(mv, Opcodes.NOP);
		seen.clear();
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name,
			String desc) {
		if ((opcode == Opcodes.PUTFIELD || opcode == Opcodes.PUTSTATIC
				|| opcode == Opcodes.GETFIELD || opcode == Opcodes.GETSTATIC)) {
			if (seen.containsKey(owner + "#" + name)
					|| seen.get(owner + "#" + name)) {
				this.inject(mv);
			}
		}
		if (opcode == Opcodes.PUTFIELD || opcode == Opcodes.PUTSTATIC
				|| opcode == Opcodes.GETFIELD || opcode == Opcodes.GETSTATIC) {
			seen.put(owner + "#" + name, true);
		}
		super.visitFieldInsn(opcode, owner, name, desc);
	}

	@Override
	public void visitCode() {
		super.visitCode();
		seen.clear();
	}

}
