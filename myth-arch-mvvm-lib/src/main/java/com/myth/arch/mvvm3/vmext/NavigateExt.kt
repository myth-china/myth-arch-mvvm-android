package com.myth.arch.mvvm3.vmext

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.myth.arch.event.*
import com.myth.arch.exception.MythIllegalStateException
import com.myth.arch.mvvm3.MythViewModel

/**
 * Navigator
 */
fun MythViewModel.startActivity(cls: Class<out AppCompatActivity>, args: Bundle? = null) {
    val name = "navigator"

    val liveData =
        getViewModelExtData(name)
            ?: MutableLiveData<Event2<Class<out AppCompatActivity>, Bundle?>>()

    liveData.postValue(Event2(cls, args))

    if (liveData.hasObservers()) {
        return
    }

    addViewModelExt(name, liveData) { view, data ->
        data.observe(view.getLifeCycleOwner(), EventObserver2 { pCls, pArgs ->
            val context = view.getContext2() ?: return@EventObserver2
            context.startActivity(Intent(context, pCls).apply { pArgs?.let { putExtras(it) } })
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
        getViewModelExtData(name)
            ?: MutableLiveData<Event3<Class<out AppCompatActivity>, Bundle?, Int>>()

    liveData.postValue(Event3(cls, args, requestCode))

    if (liveData.hasObservers()) {
        return
    }

    addViewModelExt(name, liveData) { view, data ->
        data.observe(view.getLifeCycleOwner(), EventObserver3 { pCls, pArgs, pCode ->
            when (view) {
                is FragmentActivity -> {
                    view.startActivityForResult(
                        Intent(
                            view,
                            pCls
                        ).apply { pArgs?.let { putExtras(it) } }, pCode
                    )
                }
                is Fragment -> {
                    view.startActivityForResult(
                        Intent(
                            view.context,
                            pCls
                        ).apply { pArgs?.let { putExtras(it) } }, pCode
                    )
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

    val liveData = getViewModelExtData(name) ?: MutableLiveData<Event<Boolean>>()

    liveData.postValue(Event(true))

    if (liveData.hasObservers()) {
        return
    }

    addViewModelExt(name, liveData) { view, data ->
        data.observe(view.getLifeCycleOwner(), EventObserver {
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

    val liveData = getViewModelExtData(name) ?: MutableLiveData<Event<Boolean>>()

    liveData.postValue(Event(true))

    if (liveData.hasObservers()) {
        return
    }

    addViewModelExt(name, liveData) { view, data ->
        data.observe(view.getLifeCycleOwner(), EventObserver {
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