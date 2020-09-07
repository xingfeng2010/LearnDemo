package com.administrator.learndemo.kotlin

import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Color
import android.graphics.Color.RED
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.administrator.learndemo.R
import com.administrator.learndemo.mvvm.ImageActivity
import kotlinx.android.synthetic.main.activity_kotlin.*
import retrofit2.http.HTTP

class KotlinActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin)

        printMessageWithPrefix("LISHIXING")

        println(sum(6, 8))

        var china = Country(0, "中国", "Asia")
        println(china)

        var japan = china.copy(id = 1, name = "日本")
        println(japan)

        println(sumLam(1, 2, 3))

        TEST_BUTTON.setOnClickListener {
            Toast.makeText(this, "I AM CLICK", Toast.LENGTH_SHORT).show()
        }

        testWhen(3)

        testFor()

        testKotlin()
    }

    private fun testKotlin() {
        val callIntent: Intent = Uri.parse("tel:15313222").let { number ->
            Intent(Intent.ACTION_DIAL, number)
        }

        val wetInten: Intent = Uri.parse("www.baidu.com").let { webpage ->
            Intent(Intent.ACTION_VIEW, webpage)
        }

        //发送带有附件的电子邮件
        //调用某对象的apply函数，在函数块内可以通过 this 指代该对象。返回值为该对象自己。
        Intent(Intent.ACTION_SEND).apply {
            // The intent does not have a URI, so declare the "text/plain" MIME type
            type = "PLAIN_TEXT_TYPE"
            putExtra(Intent.EXTRA_EMAIL, arrayOf("jon@example.com")) // recipients
            putExtra(Intent.EXTRA_SUBJECT, "Email subject")
            putExtra(Intent.EXTRA_TEXT, "Email message text")
            putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"))
        }

        //验证是否存在可接收 Intent 的应用
        val activities: List<ResolveInfo> = packageManager.queryIntentActivities(callIntent, PackageManager.MATCH_DEFAULT_ONLY)
        val hasMath: Boolean = activities.isEmpty()


        val testIntent: Intent = Intent(Intent.ACTION_SEND)
        with(testIntent) {
            putExtra(Intent.EXTRA_EMAIL, arrayOf("jon@example.com")) // recipients
            putExtra(Intent.EXTRA_SUBJECT, "Email subject")
            putExtra(Intent.EXTRA_TEXT, "Email message text")
            putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"))
        }

        testIntent?.run {
            putExtra(Intent.EXTRA_EMAIL, arrayOf("jon@example.com")) // recipients
            putExtra(Intent.EXTRA_SUBJECT, "Email subject")
            putExtra(Intent.EXTRA_TEXT, "Email message text")
            putExtra(Intent.EXTRA_STREAM, Uri.parse("content://path/to/email/attachment"))
        }

        val intent = Intent().apply {
            setClass(applicationContext, ImageActivity::class.java)
            putExtra("index", 0)
        }
    }

    /**
     * 使用 .. 操作符 表示一个区间，该区间是闭区间，包含开始和结束的元素
     * 然后使用 in 操作符来遍历这个区间这个区间是从小到大的，如果开始的
     * 数字比结尾的还要大，则没有意义如果想要表示 半闭区间 ，即只包含头
     * 部元素，不包含尾部可以使用 until 操作符
     */
    fun testFor() {
        // 对应 Kotlin 版本
        for (i in 0..10) {
            println("testFor:" + i)
        }

        for (i in 0 until 10) {
            println(i)
        }

        //如果想要倒序遍历，可以使用 downStep 关键字：
        for (i in 10 downTo 0) {
            println(i)
        }

        //遍历的时候 步长(step) 默认是 1，可以通过 step 关键字来指定步长：
        for (i in 10 downTo 0 step 2) {
            println(i)
        }
    }

    //任意对象作为 when 参数
    fun mix(c1: Color, c2: Color) = when (setOf(c1, c2)) {

        setOf(Color.RED, Color.YELLOW) -> Color.DKGRAY

        setOf(Color.YELLOW, Color.BLUE) -> Color.GREEN

        setOf(Color.BLUE, Color.MAGENTA) -> Color.CYAN

        //需要处理 其他 情况
        else -> throw Exception("Dirty color")
    }

    /**
     * 上面的 mix 函数比较低效，因为每次比较的时候都会创建一个或多个 set 集合
     * 如果该函数调用频繁，会创建很多临时对象
     *
     * 可以使用无参的 when 表达式来改造下：
     * 无参数的 when 表达式的条件分支必须是 boolean 类型
     */
//    fun mixOptimized(c1: Color, c2: Color) = when {
//
//    }

    /**
     *  在 Java 中对某个对象进行类型转换的时候时候，需要通过 instanceof 来判断是否可以被强转
     *  Kotlin 通过 is 关键字来判断类型，并且编译器会自动帮你做类型转换
     */
    fun test(obj: Any) {
        if (obj is String) {
            // 不需要手动做类型转换操作
            obj.substring(0, obj.length / 2)
        }
        //...
    }

    fun testWhen(pararm: Int) {
        when (pararm) {
            1 -> {
                Toast.makeText(this, "TEST:" + pararm, Toast.LENGTH_SHORT).show()
            }

            2 -> {
                Toast.makeText(this, "TEST:" + pararm, Toast.LENGTH_SHORT).show()
            }

            3 -> {
                println("You test is 3!!!")
            }
        }
    }

    fun sum(x: Int, y: Int): Int {
        return x + y
    }

    fun printMessageWithPrefix(message: String, prefix: String = "Info") {
        println("[$prefix] $message")
    }

    fun getAge3(): List<Int> {
        return listOf(22, 30, 59)
    }

    fun getAge4(): Array<Int> {
        return arrayOf(170, 80, 90)
    }

    //lambda 除了可以当作参数进行传递，还可以把 lambda 赋值给一个变量：
    //定义一个 lambda，赋值给一个变量
    val sumLam = { x: Int, y: Int, z: Int ->
        x + y + z
    }
}
