package xingfeng

import org.gradle.api.Plugin
import org.gradle.api.Project

class DateAndtimePlugin implements Plugin<Project> {

    void apply(Project project) {
        project.extensions.create("dateAndTime", DateAndTimePluginExtension)

        project.task('showTime') {
            doLast {
                println "Current time is " + new Date().format(project.dateAndTime.timeFormat)
            }
        }

        project.tasks.create('showDate') {
            doLast {
                println "Current date is " + new Date().format(project.dateAndTime.dateFormat)
            }
        }
    }
}