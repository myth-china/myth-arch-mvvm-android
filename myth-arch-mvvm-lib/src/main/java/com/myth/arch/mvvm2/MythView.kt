package com.myth.arch.mvvm2

import android.app.Activity
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.myth.arch.mvvm2.ext.MythViewModelExt

interface MythView {

    @Suppress("UNCHECKED_CAST")
    fun <T : MythViewModel> viewModelOf(cls: Class<T>): T {
        val viewModel = when (this) {
            is Fragment -> {
                ViewModelProviders.of(this).get(cls)
            }
            is AppCompatActivity -> {
                ViewModelProviders.of(this).get(cls)
            }
            else -> {
                throw IllegalArgumentException("The class must extend AppCompatActivity or Fragment.")
            }
        }

        fire(viewModel)

        return viewModel as T
    }

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