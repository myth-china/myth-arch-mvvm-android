package com.myth.arch.mvvm3

import android.os.Bundle
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.myth.arch.event.Event
import com.myth.arch.logger.MythLogger
import java.io.Closeable

class MythViewModelProvider : Closeable {

    private val tag = "MythViewModelProvider"

    private val extMap = HashMap<String, MythViewModelExt<*>>()
    private val dataMap = HashMap<String, LiveData<*>>()
    val installData = MutableLiveData<Event<Boolean>>()
    val data by lazy { Bundle() }

    fun installAllExt(view: MythView) {
        MythLogger.d(tag, "installAllExt")

        extMap.keys.forEach {
            MythLogger.d(tag, "installAllExt->$it")

            val data = dataMap[it] ?: return@forEach
            if (data.hasObservers()) {
                return@forEach
            }
            MythLogger.d(tag, "$it start install")
            extMap[it]?.invoke(view, data)
            MythLogger.d(tag, "$it end install")
        }
    }

    private fun reInstallAllExt() {
        MythLogger.d(tag, "reInstallAllExt")
        installData.postValue(Event(true))
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> installExt(name: String, data: LiveData<T>, observe: MythViewModelExt<T>) {

        MythLogger.d(tag, "installExt start $name|${data.javaClass.canonicalName}|$observe")

        if (data.hasObservers()) {
            return
        }

        extMap[name] = observe as MythViewModelExt<*>
        dataMap[name] = data
        MythLogger.d(tag, "installExt end")
        reInstallAllExt()
    }

    fun hasInstallExt(name: String): Boolean {
        return dataMap.containsKey(name)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getExtData(name: String): MutableLiveData<T>? {
        return dataMap[name] as? MutableLiveData<T>?
    }

    override fun close() {
        extMap.clear()
        dataMap.clear()
        data.clear()
    }
}