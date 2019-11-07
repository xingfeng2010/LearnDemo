package shixing

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.gradle.api.Project

public class TestMethodClassAdapter extends ClassVisitor implements Opcodes {

    Project mProject;

    public TestMethodClassAdapter(Project project, ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
        mProject = project
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        MethodVisitor mv = super.visitMethod(access, name, descriptor, signature, exceptions);
        mProject.logger.info("visiting method: $access, $name, $descriptor, $signature, $exceptions")
        return mv;
    }
}