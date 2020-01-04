package com.myth.mama.viewmodelext

import android.app.Activity
import android.content.Intent
import androidx.lifecycle.Observer
import com.myth.arch.mvvm2.MythView
import com.myth.arch.mvvm2.MythViewModel
import com.myth.arch.mvvm2.ext.MythViewModelExt
import kotlin.reflect.KClass

class NavigateExt : MythViewModelExt<KClass<Activity>>() {

    companion object {
        const val navigate = "navigate"

        fun <T : Activity> startActivity(viewModel: MythViewModel, clazz: KClass<T>) {
            @Suppress("UNCHECKED_CAST") val scaffold =
                viewModel.extMap[navigate] as? MythViewModelExt<KClass<T>>
            scaffold?.getData()?.postValue(clazz)
        }
    }

    override fun setup(view: MythView) {
        getData().observe(view.getLifeCycleOwner(), Observer {
            it ?: return@Observer
            val ctx = view.getContext2()
            view.getContext2()?.startActivity(Intent(ctx, it.java))
        })
    }
}