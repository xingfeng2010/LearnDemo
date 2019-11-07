package shixing

import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformInvocation
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes
import org.objectweb.asm.Type

class AsmTransform extends Transform {

    Project mProject;

    AsmTransform(Project project) {
        mProject = project
    }

    @Override
    String getName() {
        return AsmTransform.simpleName
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)

        println("===== ASM Transform =====")
        println("${transformInvocation.inputs}")
        println("${transformInvocation.referencedInputs}")
        println("${transformInvocation.outputProvider}")
        println("${transformInvocation.incremental}")
        println("===== ASM Transform =====")

        //当前是否是增量编译
        boolean isIncremental = transformInvocation.isIncremental()
        //消费型输入，可以从中获取jar包和class文件夹路径。需要输出给下一个任务
        Collection<TransformInput> inputs = transformInvocation.getInputs()
        //引用型输入，无需输出。
        Collection<TransformInput> referencedInputs = transformInvocation.getReferencedInputs()
        //OutputProvider管理输出路径，如果消费型输入为空，你会发现OutputProvider == null
        TransformOutputProvider outputProvider = transformInvocation.getOutputProvider()

        for (TransformInput input : inputs) {
            for (JarInput jarInput : input.getJarInputs()) {
                File dest = outputProvider.getContentLocation(
                        jarInput.getFile().getAbsolutePath(),
                        jarInput.getContentTypes(),
                        jarInput.getScopes(),
                        Format.JAR)
                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                transformJar(jarInput.getFile(), dest)
            }

            for (DirectoryInput directoryInput : input.getDirectoryInputs()) {
                println("== DI = " + directoryInput.file.listFiles().toArrayString())
                File dest = outputProvider.getContentLocation(directoryInput.getName(),
                        directoryInput.getContentTypes(), directoryInput.getScopes(),
                        Format.DIRECTORY)
                //将修改过的字节码copy到dest，就可以实现编译期间干预字节码的目的了
                //FileUtils.copyDirectory(directoryInput.getFile(), dest)
                transformDir(directoryInput.getFile(), dest)
            }
        }
    }


    private void transformJar(File input, File dest) {
        println("=== transformJar ===")
        FileUtils.copyFile(input, dest)
        println("=== transformJar input:" + input.getPath())
        println("=== transformJar dest:" + dest.getPath())
    }

    private void transformDir(File input, File dest) {
        if (dest.exists()) {
            FileUtils.forceDelete(dest)
        }
        FileUtils.forceMkdir(dest)
        String srcDirPath = input.getAbsolutePath()
        String destDirPath = dest.getAbsolutePath()
        println("=== transform dir = " + srcDirPath + ", " + destDirPath)
        for (File file : input.listFiles()) {
            String destFilePath = file.absolutePath.replace(srcDirPath, destDirPath)
            File destFile = new File(destFilePath)
            if (file.isDirectory()) {
                transformDir(file, destFile)
            } else if (file.isFile()) {
                FileUtils.touch(destFile)
                transformSingleFile(file, destFile)
            }
        }
    }

    private void transformSingleFile(File input, File dest) {
        println("=== transformSingleFile ===")
        weave(input.getAbsolutePath(), dest.getAbsolutePath())
    }

    private void weave(String inputPath, String outputPath) {
        try {
            FileInputStream is = new FileInputStream(inputPath)
            ClassReader cr = new ClassReader(is)
            ClassWriter cw = new ClassWriter(ClassWriter.COMPUTE_MAXS)
            AppJointClassVisitor visitor = new AppJointClassVisitor(cw)
            cr.accept(visitor, 0)
            is.close()
//            FileOutputStream fos = new FileOutputStream(outputPath)
//            fos.write(cw.toByteArray())
//            fos.close()
        } catch (IOException e) {
            e.printStackTrace()
        }
    }


    // Visit and change the AppJoint Class
    class AppJointClassVisitor extends ClassVisitor {

        AppJointClassVisitor(ClassVisitor cv) {
            super(Opcodes.ASM5, cv)
        }

        @Override
        MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
            MethodVisitor methodVisitor = super.visitMethod(access, name, desc, signature, exceptions)
            mProject.logger.info("visiting method: $access, $name, $desc, $signature, $exceptions")
            if (access == 2 && name == "<init>" && desc == "()V") {
                return new AddCodeToConstructorVisitor(methodVisitor)
            }
            return methodVisitor;
        }
    }

    class AddCodeToConstructorVisitor extends MethodVisitor {

        AddCodeToConstructorVisitor(MethodVisitor mv) {
            super(Opcodes.ASM5, mv)
        }

        @Override
        public void visitMethodInsn(int opcode, String owner, String name, String descriptor, boolean isInterface) {
            System.out.println("== TestMethodVisitor, owner = " + owner + ", name = " + name);
            //方法执行之前打印
            mv.visitLdcInsn(" before method exec");
            mv.visitLdcInsn(" [ASM 测试] method in " + owner + " ,name=" + name);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            mv.visitInsn(Opcodes.POP);

            super.visitMethodInsn(opcode, owner, name, descriptor, isInterface);

            //方法执行之后打印
            mv.visitLdcInsn(" after method exec");
            mv.visitLdcInsn(" method in " + owner + " ,name=" + name);
            mv.visitMethodInsn(Opcodes.INVOKESTATIC,
                    "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
            mv.visitInsn(Opcodes.POP);
        }

        /**
         * add "moduleApplications.add(new Application())" statement
         * @param applicationName internal name like "android/app/Application"
         */
        void insertApplicationAdd(String applicationName) {
            mv.visitVarInsn(Opcodes.ALOAD, 0)
            mv.visitFieldInsn(Opcodes.GETFIELD, "io/github/prototypez/appjoint/AppJoint", "moduleApplications", "Ljava/util/List;")
            mv.visitTypeInsn(Opcodes.NEW, applicationName)
            mv.visitInsn(Opcodes.DUP)
            mv.visitMethodInsn(Opcodes.INVOKESPECIAL, applicationName, "<init>", "()V", false)
            mv.visitMethodInsn(Opcodes.INVOKEINTERFACE, "java/util/List", "add", "(Ljava/lang/Object;)Z", true)
            mv.visitInsn(Opcodes.POP)
        }

        void insertRoutersPut(Tuple2<String, String> router, String impl) {
            mv.visitVarInsn(Opcodes.ALOAD, 0)
            mv.visitFieldInsn(Opcodes.GETFIELD, "io/github/prototypez/appjoint/AppJoint", "routersMap",
                    "Lio/github/prototypez/appjoint/util/BinaryKeyMap;")
            mv.visitLdcInsn(Type.getObjectType(router.first))
            mv.visitLdcInsn(router.second)
            mv.visitLdcInsn(Type.getObjectType(impl))
            mv.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "io/github/prototypez/appjoint/util/BinaryKeyMap",
                    "put", "(Ljava/lang/Object;Ljava/lang/Object;Ljava/lang/Object;)V", true)
        }
    }
}