package com.myth.arch.mvvm3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.myth.arch.exception.MythIllegalStateException

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
    val toastData = getProvider().getViewModelExtData(name) ?: MutableLiveData<(Fragment) -> Unit>()

    toastData.value = callback

    if (toastData.hasObservers()) {
        return
    }

    /**
     * 注册我们的LiveData和View建立关系
     */
    getProvider().addViewModelExt(name, toastData) { view, data ->
        data.observe(view.getLifeCycleOwner(), Observer {
            callback(view.getFragment2())
        })
    }
}

fun MythViewModel.useActivity(callback: (AppCompatActivity) -> Unit) {
    val name = "useActivity"

    val toastData =
        getProvider().getViewModelExtData(name) ?: MutableLiveData<(AppCompatActivity) -> Unit>()

    toastData.value = callback

    if (toastData.hasObservers()) {
        return
    }

    getProvider().addViewModelExt(name, toastData) { view, data ->
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

/**
 * Toast
 */
fun MythViewModel.toast(text: String) {
    val name = "toast"

    val toastData = getProvider().getViewModelExtData(name) ?: MutableLiveData<String>()

    toastData.postValue(text)

    if (toastData.hasObservers()) {
        return
    }

    getProvider().addViewModelExt(name, toastData) { view, data ->
        data.observe(view.getLifeCycleOwner(), Observer {
            val context = view.getContext2() ?: return@Observer
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }
}

/**
 * Navigator
 */
fun MythViewModel.startActivity(cls: Class<out AppCompatActivity>, args: Bundle? = null) {
    val name = "navigator"

    val liveData =
        getProvider().getViewModelExtData(name) ?: MutableLiveData<Class<out AppCompatActivity>>()

    liveData.postValue(cls)

    if (liveData.hasObservers()) {
        return
    }

    getProvider().addViewModelExt(name, liveData) { view, data ->
        data.observe(view.getLifeCycleOwner(), Observer {
            val context = view.getContext2() ?: return@Observer
            context.startActivity(Intent(context, it).apply {
                if (args != null) {
                    putExtras(args)
                }
            })
        })
    }
}

fun MythViewModel.startActivityForResult(
    cls: Class<out AppCompatActivity>,
    args: Bundle? = null,
    requestCode: Int
) {
    val name = "navigatorForResult"

    val liveData =
        getProvider().getViewModelExtData(name) ?: MutableLiveData<Class<out AppCompatActivity>>()

    liveData.postValue(cls)

    if (liveData.hasObservers()) {
        return
    }

    getProvider().addViewModelExt(name, liveData) { view, data ->
        data.observe(view.getLifeCycleOwner(), Observer {
            when (view) {
                is FragmentActivity -> {
                    view.startActivityForResult(Intent(view, it).apply {
                        if (args != null) {
                            putExtras(args)
                        }
                    }, requestCode)
                }
                is Fragment -> {
                    view.startActivityForResult(Intent(view.context, it).apply {
                        if (args != null) {
                            putExtras(args)
                        }
                    }, requestCode)
                }
                else -> {
                    throw MythIllegalStateException("Can't startActivityForResult, the view is not a FragmentActivity or Fragment!")
                }
            }
        })
    }
}

fun MythViewModel.finish() {
    val name = "finish"

    val liveData = getProvider().getViewModelExtData(name) ?: MutableLiveData<Boolean>()

    if (liveData.hasObservers()) {
        return
    }

    getProvider().addViewModelExt(name, liveData) { view, data ->
        data.observe(view.getLifeCycleOwner(), Observer {
            when (view) {
                is FragmentActivity -> {
                    view.finish()
                }
                is Fragment -> {
                    view.activity?.finish()
                }
                else -> {
                    throw MythIllegalStateException("Can't startActivityForResult, the view is not a FragmentActivity or Fragment!")
                }
            }
        })
    }
}


fun MythViewModel.popBackStack() {
    val name = "popBackStack"

    val liveData = getProvider().getViewModelExtData(name) ?: MutableLiveData<Boolean>()

    if (liveData.hasObservers()) {
        return
    }

    getProvider().addViewModelExt(name, liveData) { view, data ->
        data.observe(view.getLifeCycleOwner(), Observer {
            when (view) {
                is FragmentActivity -> {
                    view.supportFragmentManager.popBackStack()
                }
                is Fragment -> {
                    view.parentFragmentManager.popBackStack()
                }
                else -> {
                    throw IllegalStateException("Can't startActivityForResult, the view is not a FragmentActivity or Fragment!")
                }
            }
        })
    }
}