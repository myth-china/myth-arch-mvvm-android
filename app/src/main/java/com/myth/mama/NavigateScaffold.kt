package com.myth.mama

import android.app.Activity
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.myth.arch.mvvm2.MythView
import com.myth.arch.mvvm2.MythViewModel
import com.myth.arch.mvvm2.MythViewModelExtScaffold
import kotlin.reflect.KClass

class NavigateScaffold : MythViewModelExtScaffold<KClass<Activity>>() {

    companion object {
        const val navigate = "navigate"

        @Suppress("UNCHECKED_CAST")
        fun <T : Activity> startActivity(viewModel: MythViewModel, clazz: KClass<T>) {
            val scaffold = viewModel.extMap[navigate] as? MythViewModelExtScaffold<Any>
            (scaffold?.data as? MutableLiveData<KClass<T>>)?.postValue(clazz)
        }
    }

    override fun setup(view: MythView) {
        Log.e("temp", "setup")
        data.observe(view.getLifeCycleOwner(), Observer {
            val ctx = view.getContext2()
            view.getContext2()?.startActivity(Intent(ctx, it.java))
        })
    }
}