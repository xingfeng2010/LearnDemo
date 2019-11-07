package shixing.time;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AddTimeMethodVisitor extends MethodVisitor {
    private String owner;

    public AddTimeMethodVisitor(String owner, MethodVisitor mv) {
        super(Opcodes.ASM4, mv);
        this.owner = owner;
    }

    @Override
    public void visitCode() {
        mv.visitFieldInsn(Opcodes.GETSTATIC, owner, "timer", "J");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
        mv.visitInsn(Opcodes.LSUB);
        mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, "timer", "J");
    }

    @Override
    public void visitInsn(int opcode) {
        if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, owner, "timer", "J");
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J", false);
            mv.visitInsn(Opcodes.LADD);
            mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, "timer", "J");
        }
        mv.visitInsn(opcode);
    }

    @Override
    public void visitMaxs(int maxStack, int maxLocals) {
        mv.visitMaxs(maxStack + 4, maxLocals);
    }
}
