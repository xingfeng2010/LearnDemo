package com.administrator.learndemo.aop

import org.greenrobot.eventbus.EventBus

object EventHolder {
    val instance  = EventBus.builder().build()

    fun getInstanse():EventBus {
        return instance
    }
}