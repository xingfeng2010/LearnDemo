package shixing.learn

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes

class ChangeVersionAdapter extends ClassVisitor {

    ChangeVersionAdapter(ClassVisitor cv) {
        super(Opcodes.ASM4, cv)
    }

    @Override
    void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        cv.visit(Opcodes.V1_5, access, name, signature, superName, interfaces)
    }
}