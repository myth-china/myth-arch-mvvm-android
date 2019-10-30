package com.myth.arch.mvvm.ext

import androidx.lifecycle.ViewModel
import com.myth.arch.mvvm.MythViewModel
import com.myth.arch.mvvm.view.MythView


@Suppress("UNCHECKED_CAST")
fun <T : ViewModel> ViewModel.reverseInject(view: MythView): T {
    view.injectViewModel(this as MythViewModel)
    return this as T
}