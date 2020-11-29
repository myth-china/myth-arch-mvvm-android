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

fun <T> MythViewModel.easyUseExt(
    name: String,
    data: T,
    post: Boolean = true,
    action: (MythView, T) -> Unit
) {
    var liveData = getProvider().getExtData<T>(name)

    if (liveData == null) {
        liveData = MutableLiveData<T>()
        getProvider().installExt(name, liveData) { view, internalLiveData ->
            internalLiveData.observe(view.getLifeCycleOwner(), Observer {
                action(view, it)
            })
        }
    } else {
        if (post) {
            liveData.postValue(data)
        } else {
            liveData.value = data
        }
    }
}

/**
 * Use Block
 */
fun MythViewModel.useFragment(callback: (Fragment) -> Unit) {
    val name = "useFragment"

    easyUseExt(name, callback, false) { view, data ->
        if (view is Fragment) {
            data.invoke(view)
        }
    }
}

fun MythViewModel.useActivity(callback: (AppCompatActivity) -> Unit) {
    val name = "useActivity"

    val toastData =
        getProvider().getExtData(name) ?: MutableLiveData<(AppCompatActivity) -> Unit>()

    toastData.value = callback

    getProvider().installExt(name, toastData) { view, data ->
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

    val toastData = getProvider().getExtData(name) ?: MutableLiveData<String>()

    toastData.postValue(text)

    if (toastData.hasObservers()) {
        return
    }

    getProvider().installExt(name, toastData) { view, data ->
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
        getProvider().getExtData(name) ?: MutableLiveData<Class<out AppCompatActivity>>()

    liveData.postValue(cls)

    if (liveData.hasObservers()) {
        return
    }

    getProvider().installExt(name, liveData) { view, data ->
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
        getProvider().getExtData(name) ?: MutableLiveData<Class<out AppCompatActivity>>()

    liveData.postValue(cls)

    if (liveData.hasObservers()) {
        return
    }

    getProvider().installExt(name, liveData) { view, data ->
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

    val liveData = getProvider().getExtData(name) ?: MutableLiveData<Boolean>()

    if (liveData.hasObservers()) {
        return
    }

    getProvider().installExt(name, liveData) { view, data ->
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

    val liveData = getProvider().getExtData(name) ?: MutableLiveData<Boolean>()

    if (liveData.hasObservers()) {
        return
    }

    getProvider().installExt(name, liveData) { view, data ->
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