package com.myth.arch.mvvm2

import android.os.Bundle
import androidx.lifecycle.ViewModel
import com.myth.arch.mvvm.coroutine.CoroutineMain
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

open class MythViewModel : ViewModel() {

    companion object {
        var extClsMap = HashMap<String, KClass<*>>()

        fun putExtScaffold(name: String, extScaffold: KClass<*>) {
            extClsMap[name] = extScaffold
        }
    }

    val extMap = HashMap<String, Any?>()
    var data = Bundle()
    private val cm = CoroutineMain()

    init {
        extClsMap.forEach { (t, u) ->
            val instance = u.createInstance()
            this.extMap[t] = instance
        }
    }

    open fun onStarted() {
    }

    override fun onCleared() {
        super.onCleared()
        cm.destroy()
    }
}