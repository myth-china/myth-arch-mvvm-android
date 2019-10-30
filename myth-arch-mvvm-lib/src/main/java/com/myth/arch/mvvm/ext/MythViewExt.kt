package com.myth.arch.mvvm.ext

import android.widget.Toast
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import com.myth.arch.mvvm.coroutine.Event
import com.myth.arch.mvvm.coroutine.EventObserver
import com.myth.arch.mvvm.view.MythView

fun MythView.setupToast(
    lifecycleOwner: LifecycleOwner,
    toastEvent: LiveData<Event<String>>
) {
    toastEvent.observe(lifecycleOwner, EventObserver {
        try {
            Toast.makeText(getContext(), it, Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    })
}

fun MythView.setupFlower(
    lifecycleOwner: LifecycleOwner,
    flowerEvent: LiveData<Event<Boolean>>
) {

    flowerEvent.observe(lifecycleOwner, EventObserver {
        if (it) {
            getBaseBikeViewProvider().loadingDialog?.show()
        } else {
            getBaseBikeViewProvider().loadingDialog?.hide()
        }
    })
}