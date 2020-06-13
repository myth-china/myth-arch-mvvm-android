package com.myth.arch.mvvm2

import android.os.Bundle
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.myth.arch.coroutine.CoroutineMain
import com.myth.arch.mvvm2.exception.MythIllegalStateException
import com.myth.arch.mvvm2.ext.LazyMythViewModelExt
import com.myth.arch.mvvm2.ext.MythViewModelExt
import com.myth.arch.mvvm2.ext.ToastExt
import com.myth.arch.mvvm2.ext.UseActivityExt
import kotlin.reflect.KClass
import kotlin.reflect.full.createInstance

open class MythViewModel : ViewModel() {

    companion object {
        //扩展组件注册类
        var extClsMap = HashMap<String, KClass<out MythViewModelExt<*>>>()

        fun putExtScaffold(name: String, extScaffold: KClass<out MythViewModelExt<*>>) {
            extClsMap[name] = extScaffold
        }
    }

    /**
     * 扩展组件实例
     */
    val extInstanceMap = HashMap<String, Any?>()

    /**
     * 用于懒加载Ext组件，实现方式描述如下：
     * 在View中订阅此被观察者，当观察者收到通知后，
     * 会在View中调用[LazyMythViewModelExt.internalSetup]对Ext进行初始化。
     */
    val lazyExtInstanceData = MutableLiveData<LazyMythViewModelExt<*>>()

    val data by lazy { Bundle() }
    private val cmLazy = lazy { CoroutineMain() }
    private val cm by cmLazy

    init {
        extClsMap.forEach { (t, u) ->
            //过滤掉懒加载的Ext
            if (u is LazyMythViewModelExt<*>) {
                return@forEach
            }

            val instance = u.createInstance()
            this.extInstanceMap[t] = instance
        }
    }

    open fun onStarted() {
    }

    override fun onCleared() {
        super.onCleared()
        if (cmLazy.isInitialized()) {
            cm.destroy()
        }
    }

    /**
     * 使用此方法获取Ext实例，具体参考[UseActivityExt.useActivity]
     */
    @Suppress("UNCHECKED_CAST")
    fun <E : MythViewModelExt<*>> getExt(extName: String): E {
        val ext = extInstanceMap[UseActivityExt.useActivity]
            ?: throw throw MythIllegalStateException("Internal error, $extName not found.")

        return ext as E
    }

    /**
     * 使用此方法进行Ext的懒加载，具体参考[ToastExt.toast]
     */
    @Suppress("UNCHECKED_CAST")
    fun <E : LazyMythViewModelExt<*>> lazyGetExt(extName: String): E {
        val ext =
            if (extInstanceMap[extName] == null) {
                val lazyExtCls = extClsMap[extName]
                    ?: throw MythIllegalStateException("Internal error, Ext $extName not found.")
                val lazyExt = lazyExtCls.objectInstance
                    ?: throw MythIllegalStateException("Internal error, Ext $extName  can't be instance.")

                if (lazyExt is LazyMythViewModelExt) {
                    lazyExtInstanceData.postValue(lazyExt)
                } else {
                    throw MythIllegalStateException("Internal error, Ext ${lazyExtCls.qualifiedName} isn't lazy ext.")
                }

                lazyExt
            } else {
                extInstanceMap[extName] as ToastExt
            }
        return ext as E
    }
}