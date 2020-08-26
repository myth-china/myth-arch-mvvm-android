package com.myth.arch.mvvm3

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.myth.arch.exception.MythIllegalArgumentException

object MythViewProvider : MythProvider()

/**
 * 所有Activity及Fragment的父类
 */
interface MythView {

    fun getProvider(): MythViewProvider {
        return MythViewProvider
    }

    fun <T> putObj(name: String, obj: T) {
        getProvider().putObj(hashCode(), name, obj)
    }

    @Suppress("UNCHECKED_CAST")
    fun <T> getObj(name: String): T {
        return getProvider().getObj(hashCode(), name)
    }

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

    /**
     * 用于共享ViewModel实例的情况
     */
    fun <T : ViewModel> viewModelOf(fragment: Fragment, cls: Class<T>): T {
        val viewModel = ViewModelProvider(fragment).get(cls)
        fire(viewModel)
        return viewModel
    }

    /**
     * 用于共享ViewModel实例的情况
     */
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

            viewModel.initProvider()
            viewModel.config(this)
            viewModel.getConfigData().observe(
                lifecycleOwner,
                Observer {
                    viewModel.config(this)
                }
            )

            lifecycleOwner.lifecycle.addObserver(object : LifecycleObserver {

                /**
                 * 释放ViewModel中的Provider资源
                 */
                @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
                fun onDestroy() {
                    viewModel.destroyProvider()
                }
            })
        } else {
            throw IllegalStateException("MythView only can use with MythViewModel")
        }
    }

    fun getLifeCycleOwner(): LifecycleOwner {
        return when (this) {
            is AppCompatActivity -> this
            is Fragment -> this.getLifeCycleOwner()
            else -> throw subClassErrorException()
        }
    }

    fun getContext2(): Context? {
        return when (this) {
            is FragmentActivity -> this
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
            else -> throw java.lang.IllegalStateException("Root view is not a fragment!")
        }
    }

    fun subClassErrorException(): MythIllegalArgumentException {
        return MythIllegalArgumentException("The class must extend AppCompatActivity or Fragment!")
    }
}