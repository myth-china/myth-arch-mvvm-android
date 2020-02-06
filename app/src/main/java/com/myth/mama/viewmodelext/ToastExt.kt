package com.myth.mama.viewmodelext

import com.myth.arch.mvvm2.MythViewModel
import com.myth.arch.mvvm2.ext.ToastExt

fun MythViewModel.toast(text: String) {
    ToastExt.toast(this, text)
}