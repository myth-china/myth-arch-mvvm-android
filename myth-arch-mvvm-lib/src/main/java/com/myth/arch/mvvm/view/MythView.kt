package com.myth.arch.mvvm.view

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import com.myth.arch.mvvm.MythViewModel
import com.myth.arch.mvvm.ext.setupFlower
import com.myth.arch.mvvm.ext.setupToast

interface MythView {

    fun init(fragment: Fragment) {
        getBaseBikeViewProvider().fragment = fragment
        getBaseBikeViewProvider().context = fragment.context
        fragment.lifecycle.addObserver(getCoroutineMain())
    }

    fun init(activity: AppCompatActivity) {
        getBaseBikeViewProvider().activity = activity
        getBaseBikeViewProvider().context = activity
        activity.lifecycle.addObserver(getCoroutineMain())
    }

    fun getBaseBikeViewProvider(): MythViewProvider

    fun <T> getViewContainer(): T {
        //activity and fragment only have one assigned value
        return (getBaseBikeViewProvider().activity ?: getBaseBikeViewProvider().fragment) as T
    }

    fun getContext(): Context? {
        return getBaseBikeViewProvider().context
    }

    fun injectViewModel(viewModel: MythViewModel) {
        getBaseBikeViewProvider().fragment?.apply {
            setupExt(this, viewModel)
            arguments?.let {
                viewModel.data.putAll(it)
            }
            viewModel.internalOnStarted()
        }
        getBaseBikeViewProvider().activity?.apply {
            setupExt(this, viewModel)
            intent.extras?.let {
                viewModel.data.putAll(it)
            }
            viewModel.internalOnStarted()
        }
    }

    fun setupExt(lifecycleOwner: LifecycleOwner, viewModel: MythViewModel) {
        setupToast(lifecycleOwner, viewModel.toast)
        setupFlower(lifecycleOwner, viewModel.flower)
    }

    fun getCoroutineMain(): LifecycleObserver {
        return getBaseBikeViewProvider().coroutineMain
    }
}