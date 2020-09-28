package com.myth.arch.mvvm3

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myth.arch.event.Event

object MythViewModelProvider : MythProvider() {

    internal fun initMemberVar(obj: MythViewModel) {
        checkObjGC()
        addObj(obj)

        val hashCode = obj.hashCode()
        putMemberVar(hashCode, "data", Bundle())
        putMemberVar(hashCode, "extMap", HashMap<LiveData<*>, MythViewModelExt<*>>())
        putMemberVar(hashCode, "dataMap", HashMap<String, LiveData<*>>())
        putMemberVar(hashCode, "configData", MutableLiveData<Event<Boolean>>())
    }

    internal fun getData(hashCode: Int): Bundle? {
        return getMemberVar(hashCode, "data")
    }

    internal fun getExtMap(hashCode: Int): HashMap<LiveData<*>, MythViewModelExt<*>> {
        return getMemberVar(hashCode, "extMap")
    }

    internal fun getDataMap(hashCode: Int): HashMap<String, LiveData<*>> {
        return getMemberVar(hashCode, "dataMap")
    }

    internal fun getConfigData(hashCode: Int): MutableLiveData<Event<Boolean>> {
        return getMemberVar(hashCode, "configData")
    }
}

interface MythViewModel {

    /**
     * After [MythView.viewModelOf] is called.
     */
    fun onFired(data: Bundle)

    fun getProvider(): MythViewModelProvider {
        return MythViewModelProvider
    }

    /**
     * Dynamic bind an member object with this viewModel
     */
    fun <T> putMemberVar(name: String, obj: T) {
        getProvider().putMemberVar(hashCode(), name, obj)
    }

    /**
     * Get the bind  member object with this viewModel
     */
    @Suppress("UNCHECKED_CAST")
    fun <T> getMemberVar(name: String): T {
        return getProvider().getMemberVar(hashCode(), name)
    }

    fun initProvider() {
        getProvider().initMemberVar(this)
    }

    fun getData(): Bundle {
        return getProvider().getData(hashCode())
            ?: throw IllegalStateException("Do not use getData() in constructor, because it is a lifecycle method.")
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

    fun isViewModelExtAdded(name: String, view: MythView): Boolean {
        if (getMemberVar(name + view.hashCode())) {
            return true
        }

        putMemberVar(name + view.hashCode(), true)

        return false
    }
}