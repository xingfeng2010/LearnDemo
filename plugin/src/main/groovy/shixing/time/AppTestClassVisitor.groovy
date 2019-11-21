package shixing.time

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

class AppTestClassVisitor extends ClassVisitor {
    public AppTestClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM4, cv);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions)
        switch (name + desc) {
            case "sleepTime()V":
                return new AppTestMethodVisitor(methodVisitor, "initAppList", "()V", false, false)
                break
        }
        return methodVisitor;
    }
}