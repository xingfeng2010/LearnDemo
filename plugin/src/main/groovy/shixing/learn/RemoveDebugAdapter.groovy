package shixing.learn

import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes

/**
 * 通过改变visitField和visitMethod方法的access或name参数，可以改变一个字段或一个方法的修改字段或名字。
 * 除了在转发的方法调用中使用经过修改的参数之外，还可以选择根本不转发该调用。其效果就是相应的类元素被移除。
 *
 * 下面的类适配吕移除了有关外部类及内部类的信息，还删除了一个源文件的名字，也就是由其编译这个类的源文件，
 *  这一移除操作是通过在适当的访问方法中不转发任何内容而实现的。
 *
 *  这一策略对于字段和方法是无效的，因为visitField和visitMethod方法必须返回一个结果。要移除字段或方法，不得转发方法调用，并向调用者返回null.
 */
class RemoveDebugAdapter extends ClassVisitor {
    private String mName;
    public String mDesc;

    RemoveDebugAdapter(ClassVisitor cv, String name, String desc) {
        super(Opcodes.ASM4, cv)

        mName = name
        mDesc = desc
    }

    @Override
    public void visitSource(String source, String debug) {
    }

    @Override
    void visitOuterClass(String owner, String name, String desc) {
    }

    @Override
    void visitInnerClass(String name, String outerName, String innerName, int access) {
    }

    @Override
    MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
        //不要委托到下一个访问器 -> 这样将移除该方法
        if (name.equals(mName) && desc.equals(mDesc)) {
            return null
        }

        return cv.visitMethod(access, name, desc, signature, exceptions)
    }
}
