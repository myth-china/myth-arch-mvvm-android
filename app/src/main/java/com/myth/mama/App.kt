package com.myth.mama

import android.app.Application
import com.myth.arch.mvvm2.MythViewModel
import com.myth.arch.mvvm2.ext.ToastExt
import com.myth.arch.mvvm2.ext.Navigate2Ext
import com.myth.arch.mvvm2.ext.Navigate3Ext
import com.myth.arch.mvvm2.ext.NavigateExt

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MythViewModel.putExtScaffold(ToastExt.toast, ToastExt::class)
        MythViewModel.putExtScaffold(NavigateExt.navigate, NavigateExt::class)
        MythViewModel.putExtScaffold(Navigate2Ext.navigate2, Navigate2Ext::class)
        MythViewModel.putExtScaffold(Navigate3Ext.navigate3, Navigate3Ext::class)
    }
}