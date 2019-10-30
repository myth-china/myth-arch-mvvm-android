package com.myth.arch.mvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

open class MythActivity : AppCompatActivity(), MythView {
    private val viewProvider = MythViewProvider()

    override fun getBaseBikeViewProvider(): MythViewProvider {
        return viewProvider
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this as MythView).init(this)
    }
}