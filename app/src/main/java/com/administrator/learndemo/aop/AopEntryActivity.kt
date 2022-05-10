package com.administrator.learndemo.aop

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.administrator.learndemo.LearnApplication
import com.administrator.learndemo.R
import com.administrator.learndemo.aop.annotation.LoginTrace
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class AopEntryActivity : AppCompatActivity() {
    lateinit var instance: AopEntryActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        instance = this

        setContentView(R.layout.activity_aop_entry)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public fun onMainEvent(action: String) {
        when(action) {
            "startLoginActivity" -> startLoginActivity()
        }
    }

    private fun startLoginActivity() {
        startActivity(Intent(AopEntryActivity@this,LoginActivity::class.java))
    }

    @LoginTrace
    fun send(view: View?) {
        Log.e("zdh", "------------测试")
        //注意这里需要做是否登录判断，要不没有登录，也不跳转到TextActivity
        //切面主要处理没有登录的逻辑，是否登录的逻辑还是需要自己处理
        if (LearnApplication.getInstance().isLogin) {
            startActivity(Intent(this, TextActivity::class.java))
        }
    }
}