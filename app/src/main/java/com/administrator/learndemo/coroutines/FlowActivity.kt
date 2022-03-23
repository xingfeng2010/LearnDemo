package com.administrator.learndemo.coroutines

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.administrator.learndemo.R
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

/**
 * 相比于 RxJava 需要使用 observeOn、subscribeOn 来切换线程，flow 会更加简单。
 * 只需使用 flowOn，下面的例子中，展示了 flow builder 和 map 操作符都会受到 flowOn 的影响。
 *
 * 而 collect() 指定哪个线程，则需要看整个 flow 处于哪个 CoroutineScope 下。
 *
 * 我们使用Flow构建器创建了Flow，其中最基本的构建器是flowOf(),创建之后运行这个Flow需要使用terminal operators，
 * 由于terminal operator是suspend function ，因此我们需要在协程作用域内编写Flow代码，
 * 如果你不想使用这种嵌套调用而是链式调用，你可以使用 onEach{…}集合launchIn()。使用catch{}操作符来处理异常，
 * 并且当发生异常时也可以提供一个备份数据(如果你想这么做)。当上游的数据处理完或发生异常之后，使用onCompletion()
 * 来执行一些操作(感觉有点像finally)。。所有的操作符都会默认运行在调用函数的上下文中，可以使用flowOn()来切换上游的上下文
 */
class FlowActivity : AppCompatActivity() {
    companion object {
        const val TAG = "FlowTest"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_xie_cheng)

        builder()
        suspend()

        testFlow()
        testFlowOf()
        testChannelFlow()
        testFlowSwitch()
        testFlowMap()
        testFlowError()

        testFlowUseCatch()

        testCancelFlow()
    }

    //Flow的取消
    //如果我们需要定时取消Flow中代码块的执行，只需要使用withTimeoutOrNull函数添加超时时间即可，
    // 比如上述方法我们是在三秒内返回123，我们限定其在2500毫秒内执行完毕
    private fun testCancelFlow() {
        runBlocking {
            withTimeoutOrNull(2500) {
                loadData1().collect {
                    Log.i(TAG, "cancel flow value = $it")
                }
            }
        }
    }


    fun loadData1() = flow {
        Log.i(TAG, "进入加载数据的方法")
        for (i in 1..3) {
            delay(1000)
            emit(i)
        }
    }

    /**
     * 如果代码在执行过程中出现了异常，我们希望使用默认数据或者完整的备份来恢复数据流，
     * 在Rxjava中我们可以是使用 onErrorResumeNext()或者 onErrorReturn()，
     * 在Flow中我们依然可以使用catch{},但是我们需要在catch{}代码块里使用emit()来一个一个的发送备份数据，
     * 甚至如果我们愿意，可以使用emitAll()可以产生一个新的Flow，
     */
    private fun testFlowUseCatch() {
        runBlocking {
            flowOfAnimCharacter()
                    .flowOn(Dispatchers.Default)
                    .catch {
                        Log.i(TAG, "testFlowUseCatch catch 异常 重新发送")
                        emitAll(flowOf("Minato", "Hashirama"))
                    }
                    .onCompletion {
                        Log.i(TAG, "testFlowUseCatch onCompletion call")
                        it?.let { throwable -> println(throwable) }//这里由于上面处理了异常，所以这里就不会再有异常传递，这里自然也不会执行
                    }
                    .collect {
                        Log.i(TAG, "testFlowUseCatch collect value:$it")
                    }
        }
    }

    //Flow 异常处理
    private fun testFlowError() {
        runBlocking {
            try {
                flowOfAnimCharacter()
                        .map { stringToLength(it) }
                        .collect {
                            Log.i(TAG, "testFlowError flow value is:$it  thread name:${Thread.currentThread().name}")
                        }
            } catch (e: Exception) {
                Log.i(TAG, "testFlowError catch exception")
            } finally {
                Log.i(TAG, "testFlowError finally finished!!")
            }
        }
    }

    suspend fun flowOfAnimCharacter() = flow {
        emit("Madara")
        emit("Kakashi")
        //throwing some error
        throw IllegalStateException()
        emit("Jiraya")
        emit("Itachi")
        emit("Naruto")
    }

    private fun testFlowMap() {
        runBlocking {
            flowOf("A", "Ba", "ora", "pear", "fruit")
                    .map {
                        stringToLength(it)
                    }.collect {
                        Log.i(TAG, "testFlowMap flow value is:$it  thread name:${Thread.currentThread().name}")
                    }
        }
    }

    private suspend fun stringToLength(it: String): Int {
        delay(1000)
        return it.length
    }

    //flow线程切换
    private fun testFlowSwitch() {
        suspend fun myFlow() = flow {
            for (index in 1..5) {
                delay(1000);
                emit(index)

                Log.i(TAG, "testFlowSwitch flow value is:$index  thread name:${Thread.currentThread().name}")
            }
        }.map {
            it * it
        }.flowOn(Dispatchers.IO)
                .collect {
                    Log.i(TAG, "testFlowSwitch collect value is:$it  thread name:${Thread.currentThread().name}")
                }

        CoroutineScope(Dispatchers.Main).launch {
            myFlow()
        }
    }


    //创建flow
    //flowOf
    private fun testFlowOf() {
        CoroutineScope(Dispatchers.IO).launch {
            flowOf(1, 2, 3, 4, 5)
                    .onEach {
                        delay(1000)
                    }
                    .collect {
                        Log.i(TAG, "testFlowOf value is:$it")
                    }
        }
    }

    //channelFlow
    private fun testChannelFlow() {
        CoroutineScope(Dispatchers.IO).launch {
            channelFlow {
                for (i in 1..5) {
                    delay(1000)
                    Log.i(TAG, "testChannelFlow send run!!!")
                    send(i)
                }
            }.collect {
                Log.i(TAG, "testChannelFlow value is:$it")
            }
        }
    }

    //asFlow
    private fun testAsFlow() {
        CoroutineScope(Dispatchers.IO).launch {
            listOf<Int>(1, 2, 3, 4, 5)
                    .asFlow()
                    .onEach {
                        delay(1000)
                    }
                    .collect {
                        Log.i(TAG, "testAsFlow value is:$it")
                    }
        }
    }

    private fun testFlow() {
        CoroutineScope(Dispatchers.IO).launch {
            flow {
                for (i in 1..5) {
                    delay(100)
                    emit(i)
                }
            }.collect {
                Log.i(TAG, "suspend value is:$it")
            }
        }
    }

    suspend fun loadData(): List<Int> {
        delay(1000)
        return listOf(1, 2, 3)
    }

    fun suspend() {
        runBlocking {
            loadData().forEach { value ->
                Log.i(TAG, "suspend value is:$value")
            }
        }
    }

    suspend fun loadData2() {
        flow {
            for (i in 1..3) {
                delay(1000)
                emit(i)
            }
        }.onCompletion {
            Log.i(TAG, "onCompletion called!!")
        }.collect {
            Log.i(TAG, "collect data 2 is:$it")
        }
    }

    fun builder() {
        flow {
            for (i in 1..5) {
                delay(100)
                emit(i)
            }
        }
    }
}
