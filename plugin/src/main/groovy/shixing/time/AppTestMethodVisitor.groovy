package shixing.time

import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class AppTestMethodVisitor extends MethodVisitor {
    String name
    String desc
    boolean aLoad1
    boolean iLoad1

    String owner = "com/xingfeng/FingerPrintLib/asm/AppTest";

    AppTestMethodVisitor(MethodVisitor mv, String name, String desc, boolean aLoad1, boolean iLoad1) {
        super(Opcodes.ASM5, mv)
        this.name = name
        this.desc = desc
        this.aLoad1 = aLoad1
        this.iLoad1 = iLoad1
    }

    @Override
    public void visitCode() {
        super.visitCode()
        mv.visitFieldInsn(Opcodes.GETSTATIC, owner, "timer", "J")
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J")
        mv.visitInsn(Opcodes.LSUB)
        mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, "timer", "J")
    }

    @Override
    void visitInsn(int opcode) {
        if ((opcode >= Opcodes.IRETURN && opcode <= Opcodes.RETURN) || opcode == Opcodes.ATHROW) {
            mv.visitFieldInsn(Opcodes.GETSTATIC, owner, "timer", "J")
            mv.visitMethodInsn(Opcodes.INVOKESTATIC, "java/lang/System", "currentTimeMillis", "()J")
            mv.visitInsn(Opcodes.LADD)
            mv.visitFieldInsn(Opcodes.PUTSTATIC, owner, "timer", "J")
        }

        mv.visitInsn(opcode)
    }














}