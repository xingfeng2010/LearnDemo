package com.administrator.learndemo.kotlin.jetpack

import android.app.Activity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.administrator.learndemo.databinding.ActivityMyBinding
import com.administrator.learndemo.R


class MyActivity: Activity(), LifecycleOwner {
    private lateinit var mLifecycleRegistry:LifecycleRegistry

    private lateinit var myBinding: ActivityMyBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        myBinding = DataBindingUtil.setContentView<ActivityMyBinding>(this, R.layout.activity_my)

        mLifecycleRegistry = LifecycleRegistry(this)
        mLifecycleRegistry.markState(Lifecycle.State.CREATED)

        mLifecycleRegistry.addObserver(MyObserver())

        //mNameViewModel = ViewModelProvider(ViewModelProvider.AndroidViewModelFactory(this.application))
    }

    public override fun onStart() {
        super.onStart()
        mLifecycleRegistry.markState(Lifecycle.State.STARTED)
    }

    override fun getLifecycle(): Lifecycle {
        return mLifecycleRegistry
    }
}