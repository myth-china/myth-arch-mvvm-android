package com.myth.arch.mvvm.coroutine

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CoroutineMain : CoroutineScope, LifecycleObserver {
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.i("CoroutineMain", throwable.message ?: "Unknown Error")
        //throw throwable
    }

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job + exceptionHandler

    fun cancelChildren() = coroutineContext.cancelChildren()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() = job.cancel()
}