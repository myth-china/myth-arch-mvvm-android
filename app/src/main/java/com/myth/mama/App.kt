package com.myth.mama

import android.app.Application
import com.myth.arch.mvvm2.MythViewModel
import com.myth.arch.mvvm2.ToastScaffold

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        MythViewModel.putExtScaffold(ToastScaffold.toast, ToastScaffold::class)
        MythViewModel.putExtScaffold(NavigateScaffold.navigate, NavigateScaffold::class)
    }
}