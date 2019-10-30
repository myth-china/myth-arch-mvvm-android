package com.myth.arch.mvvm

import androidx.lifecycle.MutableLiveData
import com.myth.arch.mvvm.coroutine.CoroutineMain
import com.myth.arch.mvvm.coroutine.Event
import kotlinx.coroutines.launch

class RequestOnView<T>(private val builder: Builder<T>) {

    fun call() {
        builder.coroutine.launch {
            if (builder.useLoading) {
                val msg = builder.loadingMsg

                if (msg == null) {
                    builder.flower?.postValue(Event(true))
                } else {
                    builder.flower?.postValue(Event(true))
                }
            }

            val resp = builder.requestBlock

            if (builder.useLoading) {
                builder.flower?.postValue(Event(false))
            }
        }
    }

    class Builder<T> {
        lateinit var coroutine: CoroutineMain
        lateinit var requestBlock: suspend () -> T

        var flower: MutableLiveData<Event<Boolean>>? = null
        var toast: MutableLiveData<Event<String>>? = null
        var tokenInvalid: MutableLiveData<Event<Boolean>>? = null

        var successBlock: ((T) -> Unit)? = null
        var failureBlock: ((T) -> Unit)? = null
        var useFailureToast: Boolean = true
        var useLoading: Boolean = true
        var loadingMsg: String? = null

        fun coroutine(coroutine: CoroutineMain): Builder<T> {
            this.coroutine = coroutine
            return this
        }

        fun flower(flower: MutableLiveData<Event<Boolean>>?): Builder<T> {
            this.flower = flower
            return this
        }

        fun toast(toast: MutableLiveData<Event<String>>?): Builder<T> {
            this.toast = toast
            return this
        }

        fun tokenInvalid(tokenInvalid: MutableLiveData<Event<Boolean>>?): Builder<T> {
            this.tokenInvalid = tokenInvalid
            return this
        }

        fun requestBlock(requestBlock: suspend () -> T): Builder<T> {
            this.requestBlock = requestBlock
            return this
        }

        fun successBlock(successBlock: ((T) -> Unit)? = null): Builder<T> {
            this.successBlock = successBlock
            return this
        }

        fun failureBlock(failureBlock: ((T) -> Unit)? = null): Builder<T> {
            this.failureBlock = failureBlock
            return this
        }

        fun useFailureToast(useFailureToast: Boolean): Builder<T> {
            this.useFailureToast = useFailureToast
            return this
        }

        fun useLoading(useLoading: Boolean): Builder<T> {
            this.useLoading = useLoading
            return this
        }

        fun loadingMsg(loadingMsg: String?): Builder<T> {
            this.loadingMsg = loadingMsg
            return this
        }

        fun build(): RequestOnView<T> {
            return RequestOnView(this)
        }
    }
}