package com.administrator.learndemo.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import com.administrator.learndemo.R
import kotlinx.coroutines.*
import kotlin.concurrent.thread
import kotlin.coroutines.CoroutineContext

class XieChengActivity : AppCompatActivity(), CoroutineScope {
    val TAG = "XieChenTest"

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

    fun testXieChen(view: View) {
        testBlocking()
    }

    /**
     * 此时的结果居然和使用withContext几乎差不多，不是说好的并行，怎么又好像是串行执行了？

    刚只是把await()的位置改了，就出现这样的结果，所以原因应该就是在await()方法身上，点进 await() 源码看一下，
    终于明白了是怎么一回事，原来await() 仅仅被定义为 suspend 函数，因此直接在async 后面使用 await() 就和 withContext 一样，
    程序运行到这里就会被挂起直到该函数执行完成才会继续执行下一个 async 。但事实上await()也不一定导致协程会被挂起，await()
    只有在 async 未执行完成返回结果时，才会挂起协程。若 async 已经有结果了，await() 则直接获取其结果并赋值给变量，此时不会挂起协程。
     */
    private fun testAsyncWait2() {
        CoroutineScope(Dispatchers.Main).launch {
            val time1 = System.currentTimeMillis()

            val task1 = async(Dispatchers.IO) {
                delay(2000)
                Log.e("TAG", "1.执行task1.... [当前线程为：${Thread.currentThread().name}]")
                "one"  //返回结果赋值给task1
            }.await()

            val task2 = async(Dispatchers.IO) {
                delay(1000)
                Log.e("TAG", "2.执行task2.... [当前线程为：${Thread.currentThread().name}]")
                "two"  //返回结果赋值给task2
            }.await()

            Log.e("TAG", "task1 = $task1  , task2 = $task2 , 耗时 ${System.currentTimeMillis() - time1} ms  " +
                    "[当前线程为：${Thread.currentThread().name}]")
        }
    }
        /**
         * 如果同时处理多个耗时任务，且这几个任务都无相互依赖时，可以使用 async ...  await() 来处理，
         * 将上面的例子改为 async 来实现如
         */
       private  fun testAsyncWait() {
            CoroutineScope(Dispatchers.Main).launch {
                val time1 = System.currentTimeMillis()

                val task1 = async(Dispatchers.IO) {
                    delay(2000)
                    Log.e(TAG, "1.执行task1.... [当前线程为：${Thread.currentThread().name}]")
                    "one"  //返回结果赋值给task1
                }

                val task2 = async(Dispatchers.IO) {
                    delay(1000)
                    Log.e(TAG, "2.执行task2.... [当前线程为：${Thread.currentThread().name}]")
                    "two"  //返回结果赋值给task2
                }

//            Log.e(TAG, "task1 = ${task1.await()}  , task2 = ${task2.await()} , 耗时 ${System.currentTimeMillis() - time1} ms  " +
//                    "[当前线程为：${Thread.currentThread().name}]")
            }
        }

        /**
         * withContext 与 async 都可以返回耗时任务的执行结果。 一般来说，多个 withContext 任务是串行的，
         * 且withContext 可直接返回耗时任务的结果。 多个 async 任务是并行的，async 返回的是一个Deferred<T>，
         * 需要调用其await()方法获取结果。
         *
         * 多个withConext是串行执行，如上代码执行顺序为先执行task1再执行task2，共耗时两个任务的所需时间的总和。
         * 这是因为withConext是个 suspend 函数，当运行到 withConext 时所在的协程就会挂起，直到withConext执行完成后再执行下面的方法。
         *
         * 所以withConext可以用在一个请求结果依赖另一个请求结果的这种情况
         */
        fun testWithContext() {
            CoroutineScope(Dispatchers.Main).launch {
                val time1 = System.currentTimeMillis()

                val task1 = withContext(Dispatchers.IO) {
                    delay(2000)
                    Log.e(TAG, "1.执行task1.... [当前线程为：${Thread.currentThread().name}]")
                    //返回结果赋值给 task1
                    "one"
                }

                val task2 = withContext(Dispatchers.IO) {
                    delay(1000)

                    Log.e(TAG, "2.task1 执行结果是 $task1")

                    Log.e(TAG, "2.执行task2.... [当前线程为：${Thread.currentThread().name}]")
                    //返回结果赋值给 task2
                    "two"
                }

                Log.e(TAG, "task1 = $task1  , task2 = $task2 , 耗时 ${System.currentTimeMillis() - time1} ms  " +
                        "[当前线程为：${Thread.currentThread().name}]")
            }
        }

        fun testBlocking() {
            runBlocking {
                delay(100)
                for (index in 1..10) {
                    Log.e(TAG, "1.执行CoroutineScope...$index. [当前线程为：${Thread.currentThread().name}]")
                }
            }

            for (index in 1..10) {
                Log.e(TAG, ".我还在运行 $index [当前线程为：${Thread.currentThread().name}]")
            }
        }

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

            Log.i(TAG, "线程没有挂起 哈哈!")
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
                Log.i(TAG, "result = $result")

                Log.i(TAG, "Hahaha,我先运行!!")
            }
        }

        private fun setUserInfo(userInfo: String) {
            Log.i(TAG, userInfo)
        }

        private suspend fun getToken(): String {
            delay(2000)

            Log.i(TAG, "getToken run finish")
            return "token"
        }

        private suspend fun getUserInfo(token: String): String {
            delay(2000)

            Log.i(TAG, "getUserInfo run finish")
            return "$token - userInfo"
        }

        private suspend fun getResult(): Int {
            delay(3000)
            return 1
        }

        private suspend fun getResult2(): Int {
            delay(4000)
            return 2
        }
    }
