package com.myth.arch.mvvm2

fun <T> MythViewModel.fire(view: MythView): T {
    view.fire(this)
    @Suppress("UNCHECKED_CAST")
    return this as T
}