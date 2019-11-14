package shixing

import com.android.build.api.transform.*
import com.android.build.gradle.internal.pipeline.TransformManager
import org.apache.commons.codec.digest.DigestUtils
import org.apache.commons.io.FileUtils
import org.gradle.api.Project
import shixing.util.TestAsmInject


public class ActivityTransform extends Transform {
    Project project = null;

    public ActivityTransform(Project paramProject) {
        this.project = paramProject;
    }

    // 设置我们自定义的Transform对应的Task名称
    // 类似：TransformClassesWithPreDexForXXX
    @Override
    String getName() {
        return "LishixingTransform"
    }

    // 指定输入的类型，通过这里的设定，可以指定我们要处理的文件类型
    // 这样确保其他类型的文件不会传入
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {
        return TransformManager.CONTENT_CLASS;
    }
    // 指定Transform的作用范围，整个工程
    @Override
    Set<QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT;
    }


    @Override
    boolean isIncremental() {
        return false
    }

//    @Override
//    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
//        super.transform(context, inputs, referencedInputs, outputProvider, isIncremental)
//    }

    // 具体处理
    @Override
    void transform(TransformInvocation transformInvocation) throws TransformException, InterruptedException, IOException {
        super.transform(transformInvocation)
        Collection<TransformInput> inputs = transformInvocation.getInputs();

        inputs.each { TransformInput input ->
            if (input.directoryInputs != null) {
                Collection<DirectoryInput> directoryInputs = input.directoryInputs;
                directoryInputs.each { DirectoryInput directoryInput ->
                    long t1 = System.currentTimeMillis();
                    // 拦截处理 class文件，用Javassist方式
//                    TestJavassistInject.injectDir(directoryInput.file.absolutePath,
//                            directoryInput.file.absolutePath, "com/baidu/test/aop", "onCreate")
                    // 拦截处理 class文件，用ASM方式
                    TestAsmInject.injectDir(directoryInput.file.absolutePath, directoryInput.file.absolutePath);

                    long t2 = System.currentTimeMillis();
                    long t = t2 - t1;
                    System.out.println("aop time : " + t);
                    // 将input的目录复制到output指定目录
                    File dest = transformInvocation.getOutputProvider().getContentLocation(directoryInput.name,
                            directoryInput.contentTypes, directoryInput.scopes,
                            Format.DIRECTORY)
                    println("directoryInput:" + directoryInput.name + ",dest:" + dest.absolutePath);
                    FileUtils.copyDirectory(directoryInput.file, dest)

                }
            }

            if (input.jarInputs != null) {
                Collection<JarInput> jarInputs = input.jarInputs;
                for (JarInput jarInput : jarInputs) {
                    //jar文件一般是第三方依赖库jar文件

                    // 重命名输出文件（同目录copyFile会冲突）
                    String jarName = jarInput.name
                    String md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                    if (jarName.endsWith(".jar")) {
                        jarName = jarName.substring(0, jarName.length() - 4)
                    }
                    //生成输出路径
                    File dest = transformInvocation.getOutputProvider().getContentLocation(jarName + md5Name,
                            jarInput.contentTypes, jarInput.scopes, Format.JAR)
                    println("jarName:" + jarName + md5Name + ",dest:" + dest.absolutePath);
                    //将输入内容复制到输出
                    FileUtils.copyFile(jarInput.file, dest)
                }
            }

        }
    }
}