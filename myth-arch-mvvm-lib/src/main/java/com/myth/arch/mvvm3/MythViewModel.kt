package com.myth.arch.mvvm3

import android.os.Bundle
import kotlinx.coroutines.CoroutineScope

interface MythViewModel {

    fun getProvider(): MythViewModelProvider

    fun internalConfig(view: MythView) {}

    fun getData(): Bundle {
        return getProvider().data
    }

    fun launch(func: suspend CoroutineScope.() -> Unit) {
        getProvider().launch(func)
    }

     fun launchBackground(func: suspend CoroutineScope.() -> Unit) {
        getProvider().launchBackground(func)
    }
}