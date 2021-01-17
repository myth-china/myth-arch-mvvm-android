package com.myth.arch.mvvm3

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.myth.arch.exception.MythIllegalAccessException
import com.myth.arch.exception.MythIllegalArgumentException

/**
 * Parent of Activity/Fragment.
 */
interface MythView {

    /**
     * Default provider for this.
     */
    fun getProvider(): MythViewProvider

    @Suppress("UNCHECKED_CAST")
    fun <T : ViewModel> viewModelOf(cls: Class<T>): T {
        return when (this) {
            is Fragment -> {
                viewModelOf(this, cls)
            }
            is AppCompatActivity -> {
                viewModelOf(this, cls)
            }
            else -> {
                throw subClassErrorException()
            }
        }
    }

    fun <T : ViewModel> viewModelOf(fragment: Fragment, cls: Class<T>): T {
        val viewModel = ViewModelProvider(fragment).get(cls)
        fire(viewModel)
        return viewModel
    }

    fun <T : ViewModel> viewModelOf(activity: AppCompatActivity, cls: Class<T>): T {
        val viewModel = ViewModelProvider(activity).get(cls)
        fire(viewModel)
        return viewModel
    }

    fun fire(viewModel: ViewModel?) {
        var lifecycleOwner: LifecycleOwner? = null

        if (viewModel is MythViewModel) {
            if (this is AppCompatActivity) {
                lifecycleOwner = this
                this.intent.extras?.let {
                    viewModel.getData().putAll(it)
                }
            }

            if (this is Fragment) {
                lifecycleOwner = this.viewLifecycleOwner
                this.arguments?.let {
                    viewModel.getData().putAll(it)
                }
            }

            if (lifecycleOwner == null) {
                throw subClassErrorException()
            }

            viewModel.getProvider().installAllExt(this)
            viewModel.getProvider().installData.observe(
                lifecycleOwner,
                Observer {
                    viewModel.getProvider().installAllExt(this)
                }
            )
        } else {
            throw MythIllegalAccessException("MythView only can use with MythViewModel")
        }
    }

    fun getLifeCycleOwner(): LifecycleOwner {
        return when (this) {
            is AppCompatActivity -> this
            is Fragment -> this
            else -> throw subClassErrorException()
        }
    }

    fun getContext2(): Context? {
        return when (this) {
            is AppCompatActivity -> this
            is Fragment -> this.context
            else -> throw subClassErrorException()
        }
    }

    fun getActivity2(): FragmentActivity? {
        return when (this) {
            is AppCompatActivity -> this
            is Fragment -> this.activity
            else -> throw subClassErrorException()
        }
    }

    fun getFragment2(): Fragment {
        return when (this) {
            is Fragment -> this
            else -> throw MythIllegalAccessException("Root view is not a fragment!")
        }
    }

    fun subClassErrorException(): MythIllegalArgumentException {
        return MythIllegalArgumentException("The class must extend AppCompatActivity or Fragment!")
    }
}