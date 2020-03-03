package com.myth.arch.mvvm2

import android.app.Activity
import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import com.myth.arch.mvvm2.ext.MythViewModelExt

interface MythView {

    fun fire(viewModel: MythViewModel?) {
        if (this is Activity) {
            this.intent.extras?.let {
                viewModel?.data = it
            }
        }

        if (this is Fragment) {
            this.arguments?.let {
                viewModel?.data = it
            }
        }

        viewModel?.onStarted()

        viewModel?.extMap?.keys?.forEach { k ->
            @Suppress("UNCHECKED_CAST") val scaffold =
                viewModel.extMap[k] as? MythViewModelExt<Any>
            scaffold?.setup(this@MythView)
        }
    }

    fun getContext2(): Context?

    fun getFragmentManager2(): FragmentManager?

    fun getLifeCycleOwner(): LifecycleOwner
}