package com.administrator.learndemo.liefcycle

import android.util.Log
import android.view.View
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner

import androidx.lifecycle.OnLifecycleEvent
import org.jetbrains.annotations.NotNull


class CustomObserver:LifecycleObserver {

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate(@NotNull owner: LifecycleOwner?) {
        Log.i("DEBUG_TEST","Custom onCreate")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(@NotNull owner: LifecycleOwner?) {
        Log.i("DEBUG_TEST","Custom onDestroy")
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_ANY)
    fun onLifecycleChanged(@NotNull owner: LifecycleOwner?,
                           @NotNull event: Lifecycle.Event?) {
        Log.i("DEBUG_TEST","onLifecycleChanged event" + event.toString())
    }
}