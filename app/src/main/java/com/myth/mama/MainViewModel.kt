package com.myth.mama

import android.os.Bundle
import com.myth.arch.mvvm2.MythViewModel
import com.myth.arch.mvvm2.ext.startActivity
import com.myth.arch.mvvm2.ext.startActivityForResult
import com.myth.arch.mvvm2.ext.toast

class MainViewModel : MythViewModel() {

    override fun onStarted() {
        super.onStarted()
        println("on started")
        data.getString("shadiao")?.let {

        }
        toast("hello")
    }

    fun openSecondPage() {
        startActivity(SecondActivity::class, Bundle().apply {
            putString("data", "from main page")
        })
    }

    fun openSecondPageForResult() {
        startActivityForResult(
            SecondActivity::class,
            Bundle().apply {
                putString("data", "from main page")
            },
            0x11
        )
    }
}