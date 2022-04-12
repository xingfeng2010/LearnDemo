package com.administrator.learndemo.coroutines.delegate

import android.util.Log

interface Base {
    fun printMessage()
}

class BaseImpl(val x: Int): Base {
    override fun printMessage() {
        Log.i("KOTLIN_DELEGATE","value is $x")
    }

}

//class Derived: Base by BaseImpl()｛
//｝