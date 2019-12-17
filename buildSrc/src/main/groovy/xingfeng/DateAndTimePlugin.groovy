package xingfeng

import org.gradle.api.Plugin
import org.gradle.api.Project
import xingfeng.tasks.AbstractTask
import xingfeng.tasks.MyTask

class DateAndTimePlugin implements Plugin<Project> {

    static final String EXTENSION_NAME = 'HelloChen'

    void apply(Project project) {
        /**注册一个扩展容器，扩展容器可以使我们在一个闭包中为task赋值*/
        project.extensions.create(EXTENSION_NAME, BuildPluginExtension)

        testTime(project)

        addTask(project)
    }

    private void addTask(Project project) {
        project.tasks.withType(AbstractTask) {
            /** 查找扩展容器中已配置的属性*/
            def extension = project.extensions.findByName(EXTENSION_NAME)
            /** 将闭包中的扩展属性值赋给AbstractTask的相关属性*/
            conventionMapping.myId = { extension.myId }
            conventionMapping.myName = { extension.myName }
            conventionMapping.myGroup = { extension.myGroup }
        }

        addMyTask(project)
    }

    private void addMyTask(Project project) {
        /**添加一个task，task名字是myTask，定制任务是 MyTask*/
        project.task('myTask', type: MyTask) {
            /**为 MyTask 的myAddress属性赋值*/
            conventionMapping.myAddress = {
                getAddress(project)
            }
        }
    }

    private String getAddress(Project project) {
        /**首先判断project中有没有myAddress(一般在gradle.properties文件中定义)属性，没有则使用配置的属性值*/
        project.hasProperty('myAddress') ? project.myAddress : project.extensions.findByName(EXTENSION_NAME).myAddress
    }

    private void testTime(Project project) {

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

        project.task('showTest') {
            doLast {
                println "Current time is " + new Date().format(project.dateAndTime.testFormat)
            }
        }
    }
}