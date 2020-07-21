package com.myth.arch.coroutine

import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class CoroutineMain : CoroutineScope, LifecycleObserver {

    companion object {
        //自动捕获协程中抛出的异常
        var autoCatch = false
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        Log.i("CoroutineMain", throwable.message ?: "Unknown Error")
        //throw throwable
    }

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = if (autoCatch) {
            Dispatchers.Main + job + exceptionHandler
        } else {
            Dispatchers.Main + job
        }

    fun cancelChildren() = coroutineContext.cancelChildren()

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun destroy() = job.cancel()
}