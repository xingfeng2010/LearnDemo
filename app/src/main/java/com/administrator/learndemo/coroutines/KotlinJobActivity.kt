package com.administrator.learndemo.coroutines

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.administrator.learndemo.R
import kotlinx.coroutines.*

class KotlinJobActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_kotlin_job)

        case1()
    }

    private fun case1() {
        val scope = MainScope()
        scope.launch(Job()) {
            launch {
                delay(2000L)
                Log.i("KotlinJob","CancelJobActivity job1 finished")
                scope.cancel()
            }
            launch {
                delay(3000L)
                Log.i("KotlinJob","CancelJobActivity job2 finished")
            }
        }
    }
}