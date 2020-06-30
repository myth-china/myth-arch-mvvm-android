package com.myth.mama

import android.os.Bundle
import com.myth.arch.mvvm3.startActivity
import com.myth.arch.mvvm3.startActivityForResult
import com.myth.arch.mvvm3.toast
import com.myth.mama.base.BaseViewModel

class MainViewModel : BaseViewModel() {

    init {
        toast("hello")
    }

    fun openSecondPage() {
        startActivity(SecondActivity::class.java, Bundle().apply {
            putString("data", "from main page")
        })
    }

    fun openSecondPageForResult() {
        startActivityForResult(
            SecondActivity::class.java,
            Bundle().apply {
                putString("data", "from main page")
            },
            0x11
        )
    }
}