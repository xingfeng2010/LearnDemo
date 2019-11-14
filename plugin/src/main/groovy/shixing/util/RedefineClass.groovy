package shixing.util

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.commons.AdviceAdapter;

/**
 * 重新修改class
 */
public class RedefineClass {

    public static void processClass(File file) {
        System.out.println("start process class " + file.getPath());
        File optClass = new File(file.getParent(), file.getName() + ".opt");
        FileInputStream inputStream = null;
        FileOutputStream outputStream = null;
        try {
            inputStream = new FileInputStream(file);
            outputStream = new FileOutputStream(optClass);

            byte[] bytes = referHack(inputStream);
            outputStream.write(bytes);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        }
        if (file.exists()) {
            file.delete();
        }
        optClass.renameTo(file);
    }

    private static byte[] referHack(InputStream inputStream) {
        try {
            ClassReader classReader = new ClassReader(inputStream);
            ClassWriter classWriter = new ClassWriter(classReader, ClassWriter.COMPUTE_MAXS);
            ClassVisitor changeVisitor = new ChangeVisitor(classWriter);
            classReader.accept(changeVisitor, ClassReader.EXPAND_FRAMES);
            return classWriter.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }

    public static class ChangeVisitor extends ClassVisitor {
        private String owner;
        private ActivityAnnotationVisitor FileAnnotationVisitor = null;

        public ChangeVisitor(ClassVisitor cv) {
            super(Opcodes.ASM5, cv);
        }

        @Override
        public void visit(int version, int access, String name, String signature, String superName,
                          String[] interfaces) {
            super.visit(version, access, name, signature, superName, interfaces);
            this.owner = name;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            System.out.println("visitAnnotation: desc=" + desc + " visible=" + visible);
            AnnotationVisitor annotationVisitor = super.visitAnnotation(desc, visible);
            if (desc != null) {
                FileAnnotationVisitor = new ActivityAnnotationVisitor(Opcodes.ASM5, annotationVisitor, desc);
                return FileAnnotationVisitor;
            }
            return annotationVisitor;
        }

        @Override
        public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor mv = this.cv.visitMethod(access, name, desc, signature, exceptions);
            if (FileAnnotationVisitor != null) {
                return new RedefineAdvice(mv, access, owner, name, desc);
            }
            return mv;
        }
    }

    public static class RedefineAdvice extends AdviceAdapter {
        String owner = "";
        ActivityAnnotationVisitor activityAnnotationVisitor = null;

        protected RedefineAdvice(MethodVisitor mv, int access, String className, String name, String desc) {
            super(Opcodes.ASM5, mv, access, name, desc);
            owner = className;
        }

        @Override
        public AnnotationVisitor visitAnnotation(String desc, boolean visible) {
            System.out.println("visitAnnotation: desc=" + desc + " visible=" + visible);
            AnnotationVisitor annotationVisitor = super.visitAnnotation(desc, visible);
            if (desc != null) {
                activityAnnotationVisitor = new ActivityAnnotationVisitor(Opcodes.ASM5, annotationVisitor, desc);
                return activityAnnotationVisitor;
            }
            return annotationVisitor;
        }

        @Override
        protected void onMethodEnter() {
            if (activityAnnotationVisitor == null) {
                return;
            }
            super.onMethodEnter();
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESTATIC, "com/xingfeng/FingerPrintLib/asm/ActivityTimeManger",
                    activityAnnotationVisitor.value + "Start",
                    "(Landroid/app/Activity;)V");

            insertApplicationAdd("com/xingfeng/FingerPrintLib/asm/AppTest");
        }

        @Override
        protected void onMethodExit(int opcode) {
            if (activityAnnotationVisitor == null) {
                return;
            }
            super.onMethodExit(opcode);
            mv.visitVarInsn(ALOAD, 0);
            mv.visitMethodInsn(INVOKESTATIC, "com/xingfeng/FingerPrintLib/asm/ActivityTimeManger",
                    activityAnnotationVisitor.value + "End",
                    "(Landroid/app/Activity;)V");
        }

        void insertApplicationAdd(String applicationName) {
            mv.visitVarInsn(Opcodes.ALOAD, 0);
            mv.visitFieldInsn(Opcodes.GETFIELD, "com/administrator/learndemo/activitymanager/TestActivity", "moduleApplications", "Ljava/util/List;");
            mv.visitTypeInsn(Opcodes.NEW, applicationName);
            mv.visitInsn(Opcodes.DUP);
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, applicationName, "<init>", "()V", false);
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true);
            mv.visitInsn(Opcodes.POP);
        }
    }

    public static class ActivityAnnotationVisitor extends AnnotationVisitor {
        public String desc;
        public String name;
        public String value;

        public ActivityAnnotationVisitor(int api, AnnotationVisitor av, String paramDesc) {
            super(api, av);
            this.desc = paramDesc;
        }

        public void visit(String paramName, Object paramValue) {
            this.name = paramName;
            this.value = paramValue.toString();
            System.out.println("visitAnnotation: name=" + name + " value=" + value);
        }

    }
}