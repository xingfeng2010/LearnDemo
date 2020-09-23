package com.administrator.learndemo.kotlin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.administrator.learndemo.dagger.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepository @Inject constructor() {

    fun test(userId : String) : LiveData<User> {
        val data = MutableLiveData<User>()

        return data
    }
}