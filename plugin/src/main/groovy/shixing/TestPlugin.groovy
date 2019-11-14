package shixing

import org.gradle.api.Plugin
import org.gradle.api.Project
import shixing.time.TimeTransform

class TestPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
       //project.android.registerTransform(new AppJointTransform(project))
//        project.android.registerTransform(new AsmTransform(project))
//        project.android.registerTransform(new TimeTransform(project))
        project.android.registerTransform(new ActivityTransform(project))

       project.task("TestTask") {
           doLast {
               println "Hello this is TestTask!!"
           }
       }
    }
}