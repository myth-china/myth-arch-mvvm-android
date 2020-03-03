package com.myth.arch.mvvm2.ext

import android.widget.Toast
import androidx.lifecycle.Observer
import com.myth.arch.mvvm2.MythView
import com.myth.arch.mvvm2.MythViewModel

class ToastExt : MythViewModelExt<String>() {

    companion object {
        const val toast = "toast"

        fun toast(viewModel: MythViewModel, text: String) {
            @Suppress("UNCHECKED_CAST")
            val scaffold = viewModel.extMap[toast] as? MythViewModelExt<String>
            scaffold?.getData()?.postValue(text)
        }
    }

    override fun setup(view: MythView) {
        getData().observe(view.getLifeCycleOwner(), Observer {
            it ?: return@Observer
            Toast.makeText(view.getContext2(), it, Toast.LENGTH_SHORT).show()
        })
    }
}

fun MythViewModel.toast(text: String) {
    ToastExt.toast(this, text)
}