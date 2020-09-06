package com.myth.arch.mvvm3.vmext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.myth.arch.mvvm3.MythViewModel

/**
 * Use Block
 */
fun MythViewModel.useFragment(callback: (Fragment) -> Unit) {
    val name = "useFragment"

    /**
     * 从MythViewModel的MythViewModelProvider实例中获取我们的LiveData，如果此LiveData不存在，则new一个，
     * 之后会通过MythViewModelProvider.addViewModelExt(name, toastData)，将此LiveData注册到MythViewModelProvider实例中，
     * 这样我们就可以通过此LiveData发送指令给View，实现我们的逻辑
     */
    val useData = getViewModelExtData(name) ?: MutableLiveData<(Fragment) -> Unit>()

    useData.value = callback

    if (useData.hasObservers()) {
        return
    }

    /**
     * 注册我们的LiveData和View建立关系
     */
    addViewModelExt(name, useData) { view, data ->
        data.observe(view.getLifeCycleOwner(), Observer {
            callback(view.getFragment2())
        })
    }
}

fun MythViewModel.useActivity(callback: (AppCompatActivity) -> Unit) {
    val name = "useActivity"

    val toastData =
        getViewModelExtData(name) ?: MutableLiveData<(AppCompatActivity) -> Unit>()

    toastData.value = callback

    if (toastData.hasObservers()) {
        return
    }

    addViewModelExt(name, toastData) { view, data ->
        data.observe(view.getLifeCycleOwner(), Observer {
            //如果ViewModel绑定的是Fragment，那么不回调Activity代码块
            if (view is Fragment) {
                return@Observer
            }
            view.getActivity2()?.let {
                callback(it as AppCompatActivity)
            }
        })
    }
}

