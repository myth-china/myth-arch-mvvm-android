package com.myth.mama

import com.myth.arch.mvvm2.MythViewModel

class MainViewModel : MythViewModel() {

    override fun onStarted() {
        super.onStarted()
        println("on started")
        data.getString("shadiao")?.let {

        }
        toast("hello")
        toast("hi")
    }

    fun openSecondPage() {
        startActivity(SecondActivity::class)
    }
}