package com.myth.mama

import android.os.Bundle
import com.myth.mama.base.BaseViewModel
import com.myth.mama.base.startActivity
import com.myth.mama.base.startActivityForResult
import com.myth.mama.base.toast

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