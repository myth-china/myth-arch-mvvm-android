package com.myth.arch.mvvm2.ext

import androidx.lifecycle.MutableLiveData
import com.myth.arch.mvvm2.MythView
import com.myth.arch.mvvm2.MythViewModel
import java.io.Serializable

/**
 * 扩展MythViewModel，提供与View交火能力，实现功能加载
 */
fun <T> MythViewModel.fire(view: MythView): T {
    view.fire(this)
    @Suppress("UNCHECKED_CAST")
    return this as T
}

/**
 * 子类将会在MythViewModel的构造函数中被实例化，
 * 需要懒加载模式请使用[LazyMythViewModelExt]作为父类
 */
abstract class MythViewModelExt<T : Any> : Serializable {
    private var internalData = MutableLiveData<T?>()

    fun setup(view: MythView) {
        //同一时间，同一ViewModel中，每个ExtLiveData只能被订阅一次，
        //否则会出现Ext功能多次触发的情况，
        //此处抛出异常，禁止多次配置Ext组件的情况。
        if (getData().hasActiveObservers()) {
            throw IllegalAccessException("${this::class.qualifiedName} multiple setup.")
        }

        internalSetup(view)
    }

    protected abstract fun internalSetup(view: MythView)

    fun getData(): MutableLiveData<T?> {
        return internalData
    }
}

/**
 * 懒加载模式的Ext父类，只会在第一次调用时被实例化
 */
abstract class LazyMythViewModelExt<T : Any> : MythViewModelExt<T>() {

}