package com.myth.arch.mvvm2.ext

import android.app.Activity
import androidx.lifecycle.Observer
import com.myth.arch.mvvm2.MythView
import com.myth.arch.mvvm2.MythViewModel

class FinishExt : MythViewModelExt<Boolean>() {

    companion object {
        const val finish = "finish"

        fun finish(viewModel: MythViewModel, block: Boolean) {
            @Suppress("UNCHECKED_CAST")
            val scaffold = viewModel.extMap[finish] as? MythViewModelExt<Boolean>
            scaffold?.getData()?.postValue(block)
        }
    }

    override fun setup(view: MythView) {
        getData().observe(view.getLifeCycleOwner(), Observer {
            if (it == null || !it) {
                return@Observer
            }

            try {
                if (view is Activity) {
                    view.finish()
                }
            } catch (ignore: Exception) {
            }
        })
    }
}

fun MythViewModel.finish() {
    FinishExt.finish(this, true)
}

