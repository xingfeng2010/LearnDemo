package com.administrator.learndemo

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.administrator.learndemo.kotlin.Country
import java.sql.DriverManager.println

class Main2Activity() : AppCompatActivity(), IITest{
    val dialog_content_sharesdk = "为了实现分享功能，我们使用了MobTech的ShareSDK产品，请您务必仔细阅读《MobTech隐私协议》，充分了解MobTech对您个人信息的处理规则及申请权限的目的。\n\n如您同意《MobTech隐私协议》，请点击“同意”授权并使用MobTech的产品和服务。\n"

    override var num1: Int
        get() = 3
        set(value) {}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        sum(1,2)
    }

    fun HorizontalLayout(context: Context, init: LinearLayout.() -> Unit) : LinearLayout {
        return LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            init()
        }
    }

    fun daqi(func:() -> Unit){
        func()
    }

    fun sum(x:Int,y:Int){
        var count = x + y
        daqi{
            count++
            Log.i("SHIXING","$x + $y +1 = $count")
        }
    }
}
