package com.myth.arch.mvvm2

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.gson.Gson
import com.myth.arch.mvvm.coroutine.CoroutineMain
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

open class MythViewModel : ViewModel() {
    val gson = Gson()

    companion object {
        var extDataList = HashMap<String, KClass<*>>()

        fun putExtScaffold(name: String, extScaffold: KClass<*>) {
            extDataList[name] = extScaffold
        }
    }

    val extMap = HashMap<String, Any?>()
    var data = Bundle()
    private val cm = CoroutineMain()

    init {
        extDataList.forEach { (t, u) ->
            val instance = u.createInstance()
            this.extMap[t] = instance
            Log.e("temp", "$t/$instance")
        }

        Log.e("temp", gson.toJson(extMap))
    }

    open fun onStarted() {
    }

    override fun onCleared() {
        super.onCleared()
        cm.destroy()
    }
}