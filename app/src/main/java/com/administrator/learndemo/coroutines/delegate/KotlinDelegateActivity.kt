package com.administrator.learndemo.coroutines.delegate

import android.content.Context
import android.content.res.AssetManager
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.administrator.learndemo.R
import com.administrator.learndemo.coroutines.delegate.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.cancel
import kotlin.reflect.KProperty
import splitties.init.appCtx

class KotlinDelegateActivity: AppCompatActivity(),Base by BaseImpl(20), CoroutineScope by MainScope() {


    class PropertyExample{
        var p: String by PropertyDelegate()
    }

    class PropertyDelegate {
        operator fun getValue(thisRef: Any?, property: KProperty<*>):String {
            return property.name
        }

        operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
            Log.i("KOTLIN_DELEGATE","$thisRef 的 ${property.name} 属性赋值为 $value")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        cancel()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_delegate)

//        val b = BaseImpl(20)
//        val derive = Derived(b)
          printMessage()


        val example = PropertyExample()
        Log.i("KOTLIN_DELEGATE","初始属性：" + example.p)

        example.p = "Runoob"   // 调用 setValue() 函数
        Log.i("KOTLIN_DELEGATE", "After属性："+ example.p)

        var jsonStr = "{\n" +
                "\t\"name\": \"Jack\",\n" +
                "\t\"age\": 18\n" +
                "}"
        val jsonDelegate = JsonDelegate(jsonStr)

        Log.i("KOTLIN_DELEGATE", "0000000")
        val value1 by jsonDelegate.delegate("kameiliduo","name")//第一个参数是默认值，第二个参数是key
        val value2 by jsonDelegate.delegate(20)//若没有第二个参数，则key为变量名
        Log.i("KOTLIN_DELEGATE", "value1：$value1")
        Log.i("KOTLIN_DELEGATE", "value2：$value2")

        kotlin.runCatching{

        }.onSuccess{

        }.onFailure {

        }

        getAssetManager(appCtx)
    }

    private fun getAssetManager(appContext: Context):AssetManager {
        return appContext.assets
    }
}