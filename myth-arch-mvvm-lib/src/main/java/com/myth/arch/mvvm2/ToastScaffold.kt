package com.myth.arch.mvvm2

import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer

class ToastScaffold : MythViewModelExtScaffold<String>() {

    companion object {
        const val toast = "toast"

        @Suppress("UNCHECKED_CAST")
        fun toast(viewModel: MythViewModel, text: String) {
            val scaffold = viewModel.extMap[toast] as? MythViewModelExtScaffold<Any>
            (scaffold?.data as? MutableLiveData<String>)?.postValue(text)
        }
    }

    override fun setup(view: MythView) {
        data.observe(view.getLifeCycleOwner(), Observer {
            Toast.makeText(view.getContext2(), it, Toast.LENGTH_SHORT).show()
        })
    }
}