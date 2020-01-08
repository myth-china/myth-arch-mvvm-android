package com.myth.mama

import android.app.Activity
import com.myth.arch.mvvm2.MythViewModel
import com.myth.arch.mvvm2.ToastScaffold
import kotlin.reflect.KClass

fun MythViewModel.toast(text: String) {
    ToastScaffold.toast(this, text)
}

fun <T : Activity> MythViewModel.startActivity(cls: KClass<T>) {
    NavigateScaffold.startActivity(this, cls)
}