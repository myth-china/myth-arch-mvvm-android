package com.myth.arch.mvvm3

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myth.arch.event.Event

object MythViewModelProvider : MythProvider() {

    internal fun initObj(hashCode: Int) {
        putObj(hashCode, "data", Bundle())
        putObj(hashCode, "extMap", HashMap<LiveData<*>, MythViewModelExt<*>>())
        putObj(hashCode, "dataMap", HashMap<String, LiveData<*>>())
        putObj(hashCode, "configData", MutableLiveData<Event<Boolean>>())
    }

    internal fun getData(hashCode: Int): Bundle {
        return getObj(hashCode, "data")
    }

    internal fun getExtMap(hashCode: Int): HashMap<LiveData<*>, MythViewModelExt<*>> {
        return getObj(hashCode, "extMap")
    }

    internal fun getDataMap(hashCode: Int): HashMap<String, LiveData<*>> {
        return getObj(hashCode, "dataMap")
    }

    internal fun getConfigData(hashCode: Int): MutableLiveData<Event<Boolean>> {
        return getObj(hashCode, "configData")
    }
}

interface MythViewModel {

    fun getProvider(): MythViewModelProvider {
        return MythViewModelProvider
    }

    fun init() {
        getProvider().initObj(hashCode())
    }

    fun getData(): Bundle {
        return getProvider().getData(hashCode())
    }

    fun getConfigData(): MutableLiveData<Event<Boolean>> {
        return getProvider().getConfigData(hashCode())
    }

    fun config(view: MythView) {
        val extMap = getProvider().getExtMap(hashCode())

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

    private fun configAgain(hashCode: Int) {
        val configData = MythViewModelProvider.getConfigData(hashCode)

        configData.postValue(Event(true))
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> addViewModelExt(
        name: String,
        data: LiveData<T>,
        observe: MythViewModelExt<T>
    ) {
        val extMap = MythViewModelProvider.getExtMap(hashCode())
        val dataMap = getProvider().getDataMap(hashCode())

        extMap[data] = observe as MythViewModelExt<*>
        dataMap[name] = data
        configAgain(hashCode())
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getViewModelExtData(name: String): MutableLiveData<T>? {
        val dataMap = MythViewModelProvider.getDataMap(hashCode())

        return dataMap[name] as? MutableLiveData<T>?
    }
}