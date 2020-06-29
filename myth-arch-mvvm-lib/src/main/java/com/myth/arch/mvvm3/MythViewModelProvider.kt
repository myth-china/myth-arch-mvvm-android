package com.myth.arch.mvvm3

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myth.arch.event.Event
import com.myth.arch.mvvm2.MythView

class MythViewModelProvider(private val viewModel: MythViewModel) {

    private val extMap = HashMap<LiveData<*>, MythViewModelExt<*>>()
    private val dataMap = HashMap<String, LiveData<*>>()
    val configData = MutableLiveData<Event<Boolean>>()
    val data by lazy { Bundle() }

    fun config(view: MythView) {
        viewModel.internalConfig(view)
        extMap.keys.forEach {
            if (it.hasObservers()) {
                return@forEach
            }
            val data = extMap[it]
            if (data != null) {
                extMap[it]?.invoke(view, it)
            }
        }
    }

    private fun configAgain(){
        configData.postValue(Event(true))
    }

    fun <T> addExt(name: String, data: LiveData<T>, observe: MythViewModelExt<T>) {
        extMap[data] = observe as MythViewModelExt<*>
        dataMap[name] = data
        configAgain()
    }

    fun <T> getExtData(name: String): MutableLiveData<T>? {
        return dataMap[name] as? MutableLiveData<T>?
    }
}