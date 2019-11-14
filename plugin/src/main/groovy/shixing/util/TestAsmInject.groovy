package shixing.util

import java.io.File;


public class TestAsmInject {
    public static void injectDir(String topPath,String path) {
        File dir = new File(path);
        if (dir.isDirectory()) {
            for (File file : dir.listFiles()) {
                if(file.isDirectory()) {
                    injectDir(topPath,file.getPath());
                } else {
                    injectFile(topPath,file);
                }
            }
        }

    }

    private static void injectFile(String topPath, File file) {
        String filePath = file.getAbsolutePath();
        //确保当前文件是class文件，并且不是系统自动生成的class文件,且不是内部类
        if (filePath.endsWith(".class")
                && !filePath.contains("R.class")
                && !filePath.contains("BuildConfig.class")
                && !filePath.contains("\$")) {
            RedefineClass.processClass(file);
        }

    }
}