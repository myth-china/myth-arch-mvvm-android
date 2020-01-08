package com.myth.arch.mvvm2

import androidx.lifecycle.MutableLiveData
import java.io.Serializable

abstract class MythViewModelExtScaffold<T> : Serializable {
    var data = MutableLiveData<T>()

    abstract fun setup(view: MythView)

}