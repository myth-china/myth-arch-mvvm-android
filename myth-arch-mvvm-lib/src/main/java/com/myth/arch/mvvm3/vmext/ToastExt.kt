package com.myth.arch.mvvm3.vmext

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.myth.arch.mvvm3.MythViewModel

/**
 * Toast
 */
fun MythViewModel.toast(text: String) {
    val name = "toast"

    val toastData = getViewModelExtData(name) ?: MutableLiveData<String>()

    toastData.postValue(text)

    if (toastData.hasObservers()) {
        return
    }

    addViewModelExt(name, toastData) { view, data ->
        data.observe(view.getLifeCycleOwner(), Observer {
            val context = view.getContext2() ?: return@Observer
            Toast.makeText(context, it, Toast.LENGTH_LONG).show()
        })
    }
}