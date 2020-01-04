package com.myth.mama.viewmodelext

import android.app.Activity
import com.myth.arch.mvvm2.MythViewModel
import com.myth.arch.mvvm2.ext.ToastExt
import kotlin.reflect.KClass

fun MythViewModel.toast(text: String) {
    ToastExt.toast(this, text)
}

fun <T : Activity> MythViewModel.startActivity(cls: KClass<T>) {
    NavigateExt.startActivity(this, cls)
}