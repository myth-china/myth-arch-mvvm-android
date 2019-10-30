package com.myth.arch.mvvm

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myth.arch.mvvm.coroutine.CoroutineMain
import com.myth.arch.mvvm.coroutine.Event

open class MythViewModel : ViewModel() {
    val coroutine = CoroutineMain()

    var data = Bundle()

    //toast
    var toast = MutableLiveData<Event<String>>()
    //加载进度框
    var flower = MutableLiveData<Event<Boolean>>()

    fun internalOnStarted() {
        onStarted()
    }

    open fun onStarted() {
    }

    override fun onCleared() {
        super.onCleared()
        coroutine.destroy()
    }
}