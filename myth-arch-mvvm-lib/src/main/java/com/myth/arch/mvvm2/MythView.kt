package com.myth.arch.mvvm2

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.myth.arch.mvvm2.exception.MythIllegalArgumentException
import com.myth.arch.mvvm2.ext.MythViewModelExt

/**
 * 所有Activity及Fragment的父类
 */
interface MythView {

    @Suppress("UNCHECKED_CAST")
    fun <T : MythViewModel> viewModelOf(cls: Class<T>): T {
        return when (this) {
            is Fragment -> {
                viewModelOf(this, cls)
            }
            is AppCompatActivity -> {
                viewModelOf(this, cls)
            }
            else -> {
                throw MythIllegalArgumentException("The class must extend AppCompatActivity or Fragment.")
            }
        }
    }

    /**
     * 用于共享ViewModel实例的情况
     */
    fun <T : MythViewModel> viewModelOf(fragment: Fragment, cls: Class<T>): T {
        val viewModel = ViewModelProviders.of(fragment).get(cls)
        fire(viewModel)
        return viewModel
    }

    /**
     * 用于共享ViewModel实例的情况
     */
    fun <T : MythViewModel> viewModelOf(activity: AppCompatActivity, cls: Class<T>): T {
        val viewModel = ViewModelProviders.of(activity).get(cls)
        fire(viewModel)
        return viewModel
    }

    fun fire(viewModel: MythViewModel?) {
        var lifecycleOwner: LifecycleOwner? = null

        if (this is AppCompatActivity) {
            lifecycleOwner = this
            this.intent.extras?.let {
                viewModel?.data = it
            }
        }

        if (this is Fragment) {
            lifecycleOwner = this.viewLifecycleOwner
            this.arguments?.let {
                viewModel?.data = it
            }
        }

        if (lifecycleOwner == null) {
            throw MythIllegalArgumentException("The class must extend AppCompatActivity or Fragment.")
        }

        viewModel?.onStarted()

        viewModel?.extInstanceMap?.keys?.forEach { k ->
            @Suppress("UNCHECKED_CAST") val scaffold =
                viewModel.extInstanceMap[k] as? MythViewModelExt<Any>
            scaffold?.setup(this@MythView)
        }

        viewModel?.lazyExtInstanceData?.observe(lifecycleOwner, Observer {
            it.setup(this)
        })
    }

    fun getContext2(): Context?

    fun getFragmentManager2(): FragmentManager?

    fun getLifeCycleOwner(): LifecycleOwner
}