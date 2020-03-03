package com.myth.arch.mvvm2.ext

import androidx.lifecycle.MutableLiveData
import com.myth.arch.mvvm2.MythView
import com.myth.arch.mvvm2.MythViewModel
import java.io.Serializable

fun <T> MythViewModel.fire(view: MythView): T {
    view.fire(this)
    @Suppress("UNCHECKED_CAST")
    return this as T
}

abstract class MythViewModelExt<T : Any> : Serializable {
    private var internalData = MutableLiveData<T?>()

    abstract fun setup(view: MythView)

    fun getData(): MutableLiveData<T?> {
        return internalData
    }
}