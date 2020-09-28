package com.myth.arch.mvvm3.vmext

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import com.myth.arch.event.Event
import com.myth.arch.event.EventObserver
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
    val liveData = getViewModelExtData(name) ?: MutableLiveData<Event<(Fragment) -> Unit>>()

    liveData.value = Event(callback)

    if(liveData.hasObservers()){
        return
    }

    /**
     * 注册我们的LiveData和View建立关系
     */
    addViewModelExt(name, liveData) { view, data ->
        data.observe(view.getLifeCycleOwner(), EventObserver {
            if (view is Fragment) {
                it(view)
            }
        })
    }
}


fun MythViewModel.useActivity(callback: (AppCompatActivity) -> Unit) {
    val name = "useActivity"

    val liveData =
        getViewModelExtData(name) ?: MutableLiveData<Event<(AppCompatActivity) -> Unit>>()

    liveData.value = Event(callback)

    if(liveData.hasObservers()){
        return
    }

    addViewModelExt(name, liveData) { view, data ->
        data.observe(view.getLifeCycleOwner(), EventObserver {
            //如果ViewModel绑定的是Fragment，那么不回调Activity代码块
            if (view is Fragment) {
                return@EventObserver
            }
            view.getActivity2()?.let {
                it(it as AppCompatActivity)
            }
        })
    }
}

