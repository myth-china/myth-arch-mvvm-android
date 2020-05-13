package com.myth.arch.mvvm2.ext

import android.widget.Toast
import androidx.lifecycle.Observer
import com.myth.arch.mvvm2.MythView
import com.myth.arch.mvvm2.MythViewModel

class ToastExt : LazyMythViewModelExt<String>() {

    companion object {
        const val toast = "toast"

        fun toast(viewModel: MythViewModel, text: String) {
            val ext: ToastExt = viewModel.lazyGetExt(toast)
            ext.getData().postValue(text)
        }
    }

    var hasConfig = false

    override fun internalSetup(view: MythView) {
        getData().observe(view.getLifeCycleOwner(), Observer {
            it ?: return@Observer
            Toast.makeText(view.getContext2(), it, Toast.LENGTH_SHORT).show()
        })
        hasConfig = true
    }
}

fun MythViewModel.toast(text: String) {
    ToastExt.toast(this, text)
}