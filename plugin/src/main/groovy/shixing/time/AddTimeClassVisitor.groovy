package shixing.time;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class AddTimeClassVisitor extends ClassVisitor {
    private String owner;
    private boolean isInterface;

    public AddTimeClassVisitor(ClassVisitor cv) {
        super(Opcodes.ASM4, cv);
    }


    @Override
    public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        if (!isInterface && mv != null && !name.equals("<init>")) {
            mv = new AddTimeMethodVisitor(mv);
        }
        return mv;
    }
}
