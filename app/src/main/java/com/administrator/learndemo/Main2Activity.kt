package com.administrator.learndemo

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class Main2Activity : AppCompatActivity() {
    val dialog_content_sharesdk = "为了实现分享功能，我们使用了MobTech的ShareSDK产品，请您务必仔细阅读《MobTech隐私协议》，充分了解MobTech对您个人信息的处理规则及申请权限的目的。\n\n如您同意《MobTech隐私协议》，请点击“同意”授权并使用MobTech的产品和服务。\n"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)

        findViewById<TextView>(R.id.text_content).text = dialog_content_sharesdk
    }
}
