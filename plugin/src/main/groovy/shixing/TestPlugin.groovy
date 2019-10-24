package shixing

import org.gradle.api.Plugin
import org.gradle.api.Project

class TestPlugin implements Plugin<Project> {

    @Override
    void apply(Project project) {
        project.android.registerTransform(new AppJointTransform(project))

       project.task("TestTask") {
           doLast {
               println "Hello this is TestTask!!"
           }
       }
    }
}