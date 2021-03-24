package com.administrator.learndemo.coroutines

import retrofit2.Call

/**
 * 首先定义一个请求相关的支持DSL语法的接收者。
 *
 * 用协程来代替RxJava实现线程切换。
 */
class RetrofitCoroutineDSL<T> {
    var api:(Call<Result<T>>) ? = null

    internal var onSuccess: ((T) -> Unit)? = null
        private set
    internal var onFail: ((msg: String, errorCode: Int) -> Unit)? = null
        private set
    internal var onComplete: (() -> Unit)? = null
        private set

    /**
     * 获取数据成功
     * @param block (T) -> Unit
     */
    fun onSuccess(block: (T) -> Unit) {
        this.onSuccess = block
    }

    /**
     * 获取数据失败
     * @param block (msg: String, errorCode: Int) -> Unit
     */
    fun onFail(block: (msg: String, errorCode: Int) -> Unit) {
        this.onFail = block
    }

    /**
     * 访问完成
     * @param block () -> Unit
     */
    fun onComplete(block: () -> Unit) {
        this.onComplete = block
    }

    internal fun clean() {
        onSuccess = null
        onFail = null
        onComplete = null
    }
}