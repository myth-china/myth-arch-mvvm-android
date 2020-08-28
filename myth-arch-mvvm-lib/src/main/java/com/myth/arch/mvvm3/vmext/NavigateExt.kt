package com.myth.arch.mvvm3.vmext

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.myth.arch.exception.MythIllegalStateException
import com.myth.arch.mvvm3.MythViewModel

/**
 * Navigator
 */
fun MythViewModel.startActivity(cls: Class<out AppCompatActivity>, args: Bundle? = null) {
    val name = "navigator"

    val liveData =
        getViewModelExtData(name) ?: MutableLiveData<Class<out AppCompatActivity>>()

    liveData.postValue(cls)

    if (liveData.hasObservers()) {
        return
    }

    addViewModelExt(name, liveData) { view, data ->
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
        getViewModelExtData(name) ?: MutableLiveData<Class<out AppCompatActivity>>()

    liveData.postValue(cls)

    if (liveData.hasObservers()) {
        return
    }

    addViewModelExt(name, liveData) { view, data ->
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

    val liveData = getViewModelExtData(name) ?: MutableLiveData<Boolean>()

    if (liveData.hasObservers()) {
        return
    }

    addViewModelExt(name, liveData) { view, data ->
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

    val liveData = getViewModelExtData(name) ?: MutableLiveData<Boolean>()

    if (liveData.hasObservers()) {
        return
    }

    addViewModelExt(name, liveData) { view, data ->
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