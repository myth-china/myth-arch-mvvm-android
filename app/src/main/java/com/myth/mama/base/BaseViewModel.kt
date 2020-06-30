package com.myth.mama.base

import androidx.lifecycle.ViewModel
import com.myth.arch.mvvm3.MythViewModel
import com.myth.arch.mvvm3.MythViewModelProvider

open class BaseViewModel : ViewModel(), MythViewModel {

    private val mythViewModelProvider by lazy { MythViewModelProvider(this) }

    override fun getProvider(): MythViewModelProvider {
        return mythViewModelProvider
    }
}