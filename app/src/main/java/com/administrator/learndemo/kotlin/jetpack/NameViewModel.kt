package com.administrator.learndemo.kotlin.jetpack

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class NameViewModel: ViewModel() {
    val currentName: LiveData<String> by lazy {
        MutableLiveData<String>()
    }
}