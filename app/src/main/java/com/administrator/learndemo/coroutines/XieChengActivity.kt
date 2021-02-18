package com.administrator.learndemo.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.administrator.learndemo.R
import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

class XieChengActivity : AppCompatActivity(),CoroutineScope {
    val TAG = XieChengActivity::class.java.simpleName

    private lateinit var job: Job

    suspend fun main() {
        Log.i(TAG, "111")
        var job = GlobalScope.launch(start = CoroutineStart.LAZY) {
            Log.i(TAG, "222")
        }
        Log.i(TAG, "333")
        Log.i(TAG, "444")
    }

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_xie_cheng)

        //testSuspend()
        //testLaunch()
        //testAsync()

        job = Job()

       // testXieCheng()

      //  testXieChengSuspend()

        var thread = thread {
            Log.i(TAG, "thread is runing !!!")
        }

        //thread.start()

        GlobalScope.launch {
            main()
        }

        testContext()
    }

    private fun testContext() {
        //val coroutineScope = CoroutineScope(context)
    }

    override fun onDestroy() {
        super.onDestroy()

        job.cancel()
    }

    /**
     * +是CoroutineContext中的运算符重载，包含两者的上下文
     *
     * 在Activity中可以直接调用扩展函数retrofit来调用网络请求：
     */
    private fun testXieCheng() {
//        retrofit<User> {
//            api = RetrofitCreater.create(Api::class.java).login()
//        }
    }

    /**
    协程体是一个用suspend关键字修饰的一个无参，无返回值的函数类型。被suspend修饰的函数称为挂起函数,与之对应的是关键字resume（恢复），
    注意：挂起函数只能在协程中和其他挂起函数中调用，不能在其他地方使用。

    suspend函数会将整个协程挂起，而不仅仅是这个suspend函数，也就是说一个协程中有多个挂起函数时，它们是顺序执行的
     */
    private fun testSuspend() {
        GlobalScope.launch {
            val token = getToken()
            middleSuspend()
            val userInfo = getUserInfo(token)
            setUserInfo(userInfo)

            repeat(8) {
                Log.i(TAG, "主线程执行 $it")
            }
        }
    }

    private fun middleSuspend() {
        Log.i(TAG, "middleSuspend run finish")
    }

    private fun testLaunch(): Job {
        Log.i(TAG, "主线程id：${mainLooper.thread.id}")

        val job = GlobalScope.launch {
            delay(1000)
            Log.i(TAG, "协程执行结束 -- 线程id：${Thread.currentThread().id}")
        }
        Log.i(TAG, "主线程执行结束")

        return job
    }

    private fun testBlock() = runBlocking {
        repeat(8) {
            Log.i(TAG, "协程执行$it 线程id：${Thread.currentThread().id}")
            delay(1000)
        }
    }


    /**
     * async跟launch的用法基本一样，区别在于：async的返回值是Deferred，将最后一个封装成了该对象。
     * async可以支持并发，此时一般都跟await一起使用
     *
     * async是不阻塞线程的,也就是说getResult1和getResult2是同时进行的，所以获取到result的时间是4s，而不是7s。
     */
    private fun testAsync() {
        GlobalScope.launch {
            val result1 = GlobalScope.async {
                getResult()
            }

            val result2 = GlobalScope.async {
                getResult2()
            }

            val result = result1.await() + result2.await()
            Log.i(TAG,"result = $result")

            Log.i(TAG,"Hahaha,我先运行!!")
        }
    }

    private fun setUserInfo(userInfo: String) {
        Log.i(TAG,userInfo)
    }

    private suspend fun getToken():String {
        delay(2000)

        Log.i(TAG, "getToken run finish")
        return "token"
    }

    private suspend fun getUserInfo(token: String):String {
        delay(2000)

        Log.i(TAG, "getUserInfo run finish")
        return "$token - userInfo"
    }

    private suspend fun getResult():Int {
        delay(3000)
        return 1
    }

    private suspend fun getResult2():Int {
        delay(4000)
        return 2
    }
}
