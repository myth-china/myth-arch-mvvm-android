package com.myth.arch.mvvm3

import android.os.Bundle

interface MythViewModel {

    fun getProvider(): MythViewModelProvider

    fun internalConfig(view: MythView) {}

    fun getData(): Bundle {
        return getProvider().data
    }
}