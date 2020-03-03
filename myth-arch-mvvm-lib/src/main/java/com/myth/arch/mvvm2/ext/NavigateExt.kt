package com.myth.arch.mvvm2.ext

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.myth.arch.mvvm2.MythView
import com.myth.arch.mvvm2.MythViewModel
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

data class Navigate2Data<T : Activity>(
    val kClass: KClass<T>,
    val data: Bundle? = null
)

class Navigate2Ext<T : Activity> : MythViewModelExt<Navigate2Data<T>>() {

    companion object {
        const val navigate2 = "navigate2"

        fun <T : Activity> startActivity(viewModel: MythViewModel, data: Navigate2Data<T>) {
            @Suppress("UNCHECKED_CAST") val scaffold =
                viewModel.extMap[navigate2] as? MythViewModelExt<Navigate2Data<T>>
            scaffold?.getData()?.postValue(data)
        }
    }

    override fun setup(view: MythView) {
        getData().observe(view.getLifeCycleOwner(), Observer {
            it ?: return@Observer

            if (view is Fragment) {
                view.startActivity(Intent(
                    view.getContext2(),
                    it.kClass.java
                ).apply {
                    it.data?.let { data -> putExtras(data) }
                })
            }

            if (view is Activity) {
                view.startActivity(Intent(
                    view.getContext2(),
                    it.kClass.java
                ).apply {
                    it.data?.let { data -> putExtras(data) }
                })
            }
        })
    }
}


data class Navigate3Data<T : Activity>(
    val kClass: KClass<T>,
    val data: Bundle? = null,
    val requestCode: Int
)

class Navigate3Ext<T : Activity> : MythViewModelExt<Navigate3Data<T>>() {

    companion object {
        const val navigate3 = "navigate3"

        fun <T : Activity> startActivityForResult(
            viewModel: MythViewModel,
            data: Navigate3Data<T>
        ) {
            @Suppress("UNCHECKED_CAST")
            val scaffold = viewModel.extMap[navigate3] as? MythViewModelExt<Navigate3Data<T>>
            scaffold?.getData()?.postValue(data)
        }
    }

    override fun setup(view: MythView) {
        getData().observe(view.getLifeCycleOwner(), Observer {
            it ?: return@Observer

            if (view is Fragment) {
                view.startActivityForResult(
                    Intent(
                        view.getContext2(),
                        it.kClass.java
                    ).apply {
                        it.data?.let { data -> putExtras(data) }
                    }, it.requestCode
                )
            }

            if (view is Activity) {
                view.startActivityForResult(
                    Intent(
                        view.getContext2(),
                        it.kClass.java
                    ).apply {
                        it.data?.let { data -> putExtras(data) }
                    }, it.requestCode
                )
            }
        })
    }
}

fun <T : Activity> MythViewModel.startActivity(cls: KClass<T>) {
    NavigateExt.startActivity(this, cls)
}

fun <T : Activity> MythViewModel.startActivity(cls: KClass<T>, data: Bundle) {
    Navigate2Ext.startActivity(
        this,
        Navigate2Data(cls, data)
    )
}

fun <T : Activity> MythViewModel.startActivityForResult(
    cls: KClass<T>,
    data: Bundle,
    requestCode: Int
) {
    Navigate3Ext.startActivityForResult(
        this,
        Navigate3Data(cls, data, requestCode)
    )
}