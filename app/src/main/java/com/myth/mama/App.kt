package com.myth.mama

import android.app.Application
import com.myth.arch.mvvm2.MythViewModel
import com.myth.arch.mvvm2.ext.ToastExt
import com.myth.mama.viewmodelext.NavigateExt

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MythViewModel.putExtScaffold(ToastExt.toast, ToastExt::class)
        MythViewModel.putExtScaffold(NavigateExt.navigate, NavigateExt::class)
    }
}