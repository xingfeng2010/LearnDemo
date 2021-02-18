package com.administrator.learndemo.kotlin.jetpack

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

class NameViewModel: ViewModel() {
    val currentName: LiveData<String> by lazy {
        MutableLiveData<String>()
    }

    fun launchDataLoad() {
        viewModelScope.launch {

        }
    }
}