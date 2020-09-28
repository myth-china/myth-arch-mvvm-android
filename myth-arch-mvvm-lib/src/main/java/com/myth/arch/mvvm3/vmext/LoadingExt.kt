package com.myth.arch.mvvm3.vmext

import android.app.ProgressDialog
import androidx.lifecycle.MutableLiveData
import com.myth.arch.event.Event
import com.myth.arch.event.EventObserver
import com.myth.arch.mvvm3.MythViewModel

/**
 * Loading dialog
 */
fun MythViewModel.showLoading(show: Boolean) {
    val name = "loading"

    val loadingData = getViewModelExtData(name) ?: MutableLiveData<Event<Boolean>>()

    loadingData.postValue(Event(show))

    if (loadingData.hasObservers()) {
        return
    }

    addViewModelExt(name, loadingData) { view, data ->
        data.observe(view.getLifeCycleOwner(), EventObserver {
            val progressDialog = if (view.hasMemberVar(name)) {
                view.getMemberVar(name)
            } else {
                val pd = ProgressDialog(view.getContext2())
                view.pubMemberVar(name, pd)
                pd
            }

            if (it) {
                progressDialog.show()
            } else {
                progressDialog.dismiss()
            }
        })
    }
}
