package com.administrator.learndemo.kotlin

@JvmOverloads
fun <T> joinToString(collection: Collection<T>,
                     separator:String = ",",
                     prefix:String = "",
                     postfix:String = ""):String{
    val result = StringBuilder(prefix)
    for ((index,element) in collection.withIndex()){
        if (index > 0)
            result.append(separator)
        result.append(element)
    }
    result.append(postfix)
    return result.toString()
}

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