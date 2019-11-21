package shixing.time

import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.google.common.collect.Sets
import org.objectweb.asm.*
import groovy.io.FileType
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import shixing.util.Compressor
import shixing.util.Decompression

class TimeTransform extends Transform {

    Project mProject;

    public TimeTransform(Project project) {
        mProject = project
    }

    def myTimeClassFile

    File myTimeOutputFile

    def appTestClassFile

    File appTestOutputFile

    @Override
    String getName() {
        return TimeTransform.getSimpleName()
    }

    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return Collections.singleton(QualifiedContent.DefaultContentType.CLASSES)
    }

    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return Sets.immutableEnumSet(
                QualifiedContent.Scope.PROJECT,
                QualifiedContent.Scope.SUB_PROJECTS,
                QualifiedContent.Scope.EXTERNAL_LIBRARIES
        )
    }

    @Override
    boolean isIncremental() {
        return true
    }

    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        def repackageActions = [];
        def modules = [];

        transformInvocation.inputs.each { input ->
            //Find specified class
            input.jarInputs.each { jarInput ->
                if (!jarInput.file.exists()) return
                mProject.logger.info("遍历JAR包：" + jarInput.name);
                if (jarInput.name.startsWith(":FingerPrintLib")) {
                    modules.add(jarInput)
                } else {
                    // normal jars, just copy it to destination
                    def dest = transformInvocation.outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                    mProject.logger.info("jar output path:" + dest.getAbsolutePath())
                    FileUtils.copyFile(jarInput.file, dest)
                }
            }

            // Find annotated classes in dir
            input.directoryInputs.each { dirInput ->
                mProject.logger.info("dirInput.file :" + dirInput.file)

                def outDir = transformInvocation.outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY)
                // dirInput.file is like "build/intermediates/classes/debug"
                int pathBitLen = dirInput.file.toString().length()

                def callback = { File it ->
                    if (it.exists()) {
                        def path = "${it.toString().substring(pathBitLen)}"
                        if (it.isDirectory()) {
                            new File(outDir, path).mkdirs()
                        } else {
                            def output = new File(outDir, path)
//                            findAnnotatedClasses(it, output)
                            if (!output.parentFile.exists()) output.parentFile.mkdirs()
                            output.bytes = it.bytes
                        }
                    }
                }

                if (dirInput.changedFiles != null && !dirInput.changedFiles.isEmpty()) {
                    dirInput.changedFiles.keySet().each(callback)
                }
                if (dirInput.file != null && dirInput.file.exists()) {
                    dirInput.file.traverse(callback)
                }
            }
        }

        modules.each { JarInput jarInput ->
            def repackageAction = traversalJar(
                    transformInvocation,
                    jarInput,
                    { File outputFile, File inputFile -> return findMyTimeClass(inputFile, outputFile) }
            )

            if (repackageAction) repackageActions.add(repackageAction)
        }

        if (myTimeClassFile == null) {
            throw new RuntimeException("com/xingfeng/FingerPrintLib/asm/Time not found!!")
        }

        def inputStream = new FileInputStream(myTimeClassFile)
        ClassReader cr = new ClassReader(inputStream)
        ClassWriter cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
        ClassVisitor visitor = new AddTimeClassVisitor(cw)
        cr.accept(visitor, 0)
        myTimeOutputFile.bytes = cw.toByteArray()
        inputStream.close()

        inputStream = new FileInputStream(appTestClassFile)
        cr = new ClassReader(inputStream)
        cw = new ClassWriter(cr, ClassWriter.COMPUTE_MAXS)
        visitor = new AppTestClassVisitor(cw)
        cr.accept(visitor, 0)
        appTestOutputFile.bytes = cw.toByteArray()
        inputStream.close()

        // After all class modifications are done, repackage all deferred jar repackage
        repackageActions.each { Closure action -> action.call() }
    }


    /**
     * Unzip jarInput, traversal all files, do something, and repackage it back to jar(Optional)
     * @param transformInvocation From Transform Api
     * @param jarInput From Transform Api
     * @param closure something you wish to do while traversal, return true if you want to repackage later
     * @return repackage action if you return true in closure, null if you return false in every traversal
     */
    Closure traversalJar(TransformInvocation transformInvocation, JarInput jarInput, Closure closure) {
        def jarName = jarInput.name

        mProject.logger.info("遍历JAR包：" + jarName);
        File unzipDir = new File(
                jarInput.file.getParent(),
                jarName.replace(":", "") + "_unzip")
        if (unzipDir.exists()) {
            unzipDir.delete()
        }

        mProject.logger.info("遍历JAR包路径：" + unzipDir.getAbsolutePath());
        unzipDir.mkdirs()
        Decompression.uncompress(jarInput.file, unzipDir)

        File repackageFolder = new File(
                jarInput.file.getParent(),
                jarName.replace(":", "") + "_repackage"
        )

        FileUtils.copyDirectory(unzipDir, repackageFolder)

        boolean repackageLater = false
        unzipDir.eachFileRecurse(FileType.FILES, { File it ->
            File outputFile = new File(repackageFolder, it.absolutePath.split("_unzip")[1])
            boolean result = closure.call(outputFile, it)
            if (result) repackageLater = true
        })

        def repackageAction = {
            def dest = transformInvocation.outputProvider.getContentLocation(
                    jarName, jarInput.contentTypes, jarInput.scopes, Format.JAR)
            mProject.logger.info("恢复JAR包路径：" + dest.getAbsolutePath());
            Compressor zc = new Compressor(dest.getAbsolutePath())
            zc.compress(repackageFolder.getAbsolutePath())
        }

        if (!repackageLater) {
            repackageAction.call()
        } else {
            return repackageAction
        }
    }

    /**
     * Find the AppJoint class, this method doesn't change the class file
     * @param file : the class file to be checked
     * @param outputFile : where the modified class should be output to
     * @return whether the file is AppJoint class file
     */
    boolean findMyTimeClass(File file, File outputFile) {
        if (!file.exists() || !file.name.endsWith(".class")) {
            return
        }
        boolean found = false;
        def inputStream = new FileInputStream(file)
        ClassReader cr = new ClassReader(inputStream)
        mProject.logger.info("找到的类名 Name：" + cr.className);
        cr.accept(new ClassVisitor(Opcodes.ASM4) {
            @Override
            void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
                super.visit(version, access, name, signature, superName, interfaces)
                if (name == "com/xingfeng/FingerPrintLib/asm/Time") {
                    myTimeClassFile = file
                    myTimeOutputFile = outputFile
                    found = true
                } else if (name == "com/xingfeng/FingerPrintLib/asm/AppTest") {
                    appTestClassFile = file
                    appTestOutputFile = outputFile
                    found = true
                }
            }
        }, 0)
        inputStream.close()
        return found
    }
}