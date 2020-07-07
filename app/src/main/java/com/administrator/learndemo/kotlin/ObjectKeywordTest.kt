package com.administrator.learndemo.kotlin

/**
 * 如果使用 val 来定义，而没有使用 const 那么该属性是一个私有常量
   如果使用 const 和 val 来定义则是一个公共常量
   如果使用 var 来定义，则是一个静态变量
 */
class ObjectKeywordTest {
    //伴生对象
    companion object {
        //公有常量
        const val FEMALE: Int = 0
        const val MALE: Int = 1

        //私有常量
        val GENDER: Int = FEMALE

        //私有静态变量
        var username: String = "chiclaim"

        //静态方法
        fun run() {
            println("run...")
        }
    }
}