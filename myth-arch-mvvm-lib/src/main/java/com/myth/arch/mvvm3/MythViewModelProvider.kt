package com.myth.arch.mvvm3

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myth.arch.event.Event

class MythViewModelProvider(private val viewModel: MythViewModel) {

    private val extMap = HashMap<String, MythViewModelExt<*>>()
    private val dataMap = HashMap<String, LiveData<*>>()
    val installData = MutableLiveData<Event<Boolean>>()
    val data by lazy { Bundle() }

    fun installAllExt(view: MythView) {

        viewModel.internalConfig(view)

        extMap.keys.forEach {
            val data = dataMap[it] ?: return@forEach
            if (data.hasObservers()) {
                return@forEach
            }
            extMap[it]?.invoke(view, data)
        }
    }

    private fun reInstallAllExt() {
        installData.postValue(Event(true))
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> installExt(name: String, data: LiveData<T>, observe: MythViewModelExt<T>) {
        if (data.hasObservers()) {
            return
        }

        extMap[name] = observe as MythViewModelExt<*>
        dataMap[name] = data
        reInstallAllExt()
    }

    fun hasInstallExt(name: String): Boolean {
        return dataMap.containsKey(name)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getExtData(name: String): MutableLiveData<T>? {
        return dataMap[name] as? MutableLiveData<T>?
    }
}