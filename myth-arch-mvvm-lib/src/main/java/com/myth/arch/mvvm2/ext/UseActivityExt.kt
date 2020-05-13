package com.myth.arch.mvvm2.ext

import android.app.Activity
import androidx.lifecycle.Observer
import com.myth.arch.mvvm2.MythView
import com.myth.arch.mvvm2.MythViewModel

class UseActivityExt : MythViewModelExt<(Activity) -> Unit>() {

    companion object {
        const val useActivity = "UseActivity"

        fun useActivity(viewModel: MythViewModel, block: (Activity) -> Unit) {
            viewModel.getExt<UseActivityExt>(useActivity).getData().postValue(block)
        }
    }

    override fun internalSetup(view: MythView) {
        getData().observe(view.getLifeCycleOwner(), Observer {
            it ?: return@Observer
            view.getContext2() ?: return@Observer
            if (view.getContext2() is Activity) {
                it.invoke(view.getContext2() as Activity)
            }
        })
    }
}

fun MythViewModel.useActivity(block: (Activity) -> Unit) {
    UseActivityExt.useActivity(this, block)
}
