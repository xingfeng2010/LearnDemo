package shixing.learn

import com.android.build.api.transform.Format
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInvocation
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.gradle.api.Project
import org.objectweb.asm.ClassReader
import jdk.internal.org.objectweb.asm.Opcodes
import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Type
import org.objectweb.asm.util.TraceClassVisitor

class LearnTransform extends Transform {

    private Project mProject

    LearnTransform(Project project) {
        mProject = project
    }

    @Override
    String getName() {
        return LearnTransform.getSimpleName()
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
        transformInvocation.inputs.each { input ->
            input.jarInputs.each { jarInput ->
                if (!jarInput.file.exists()) {
                    return
                }

                def dest = transformInvocation.outputProvider.getContentLocation(jarInput.name, jarInput.contentTypes, jarInput.scopes, Format.JAR)
                FileUtils.copyFile(jarInput.file, dest)
            }

            input.directoryInputs.each { dirInput ->
                def outDir = transformInvocation.outputProvider.getContentLocation(dirInput.name, dirInput.contentTypes, dirInput.scopes, Format.DIRECTORY)

                int pathBitLen = dirInput.file.toString().length()

                def callback = { File it ->
                    if (it.exists()) {
                        def path = "${it.toString().substring(pathBitLen)}"
                        if (it.isDirectory()) {
                            new File(outDir, path).mkdirs()
                        } else {
                            def output = new File(outDir, path)
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
    }
}