package com.myth.mama.base

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.myth.arch.mvvm3.MythViewModel
import com.myth.mama.performance.TimePerformance

/**
 * Use Block
 */
internal fun BaseViewModel.useFragment(callback: (Fragment) -> Unit) {
    val name = "useFragment"

    val toastData = provider.getViewModelExtData(name) ?: MutableLiveData<(Fragment) -> Unit>()

    toastData.value = callback

    if (toastData.hasObservers()) {
        return
    }

    provider.addViewModelExt(name, toastData) { view, data ->
        data.observe(view.getLifeCycleOwner(), Observer {
            callback(view.getFragment2())
        })
    }
}

internal fun BaseViewModel.useActivity(callback: (AppCompatActivity) -> Unit) {
    val name = "useActivity"

    val toastData =
        provider.getViewModelExtData(name) ?: MutableLiveData<(AppCompatActivity) -> Unit>()

    toastData.value = callback

    if (toastData.hasObservers()) {
        return
    }

    provider.addViewModelExt(name, toastData) { view, data ->
        data.observe(view.getLifeCycleOwner(), Observer {
            view.getActivity2()?.let {
                callback(it as AppCompatActivity)
            }
        })
    }
}

/**
 * Toast
 */
internal fun BaseViewModel.toast(text: String) {
    val name = "toast"

    val timeRecorder = TimePerformance.getTimeRecorder(name)
    timeRecorder.start()

    val toastData = provider.getViewModelExtData(name) ?: MutableLiveData<String>()

    toastData.postValue(text)

    if (toastData.hasObservers()) {
        return
    }

    provider.addViewModelExt(name, toastData) { view, data ->
        data.observe(view.getLifeCycleOwner(), Observer {
            val context = view.getContext2() ?: return@Observer
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }

    timeRecorder.print()
}

/**
 * Navigator
 */
internal fun BaseViewModel.startActivity(cls: Class<out AppCompatActivity>, args: Bundle? = null) {
    val name = "navigator"

    val liveData =
        provider.getViewModelExtData(name) ?: MutableLiveData<Class<out AppCompatActivity>>()

    liveData.postValue(cls)

    if (liveData.hasObservers()) {
        return
    }

    provider.addViewModelExt(name, liveData) { view, data ->
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

internal fun BaseViewModel.startActivityForResult(
    cls: Class<out AppCompatActivity>,
    args: Bundle? = null,
    requestCode: Int
) {
    val name = "navigatorForResult"


    val timeRecorder = TimePerformance.getTimeRecorder(name)
    timeRecorder.start()

    val liveData =
        provider.getViewModelExtData(name) ?: MutableLiveData<Class<out AppCompatActivity>>()

    liveData.postValue(cls)

    if (liveData.hasObservers()) {
        return
    }

    provider.addViewModelExt(name, liveData) { view, data ->
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
                    throw IllegalStateException("Can't startActivityForResult, the view is not a FragmentActivity or Fragment!")
                }
            }
        })
    }

    timeRecorder.print()
}

internal fun MythViewModel.finish() {
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
                    throw IllegalStateException("Can't startActivityForResult, the view is not a FragmentActivity or Fragment!")
                }
            }
        })
    }
}


internal fun MythViewModel.popBackStack() {
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