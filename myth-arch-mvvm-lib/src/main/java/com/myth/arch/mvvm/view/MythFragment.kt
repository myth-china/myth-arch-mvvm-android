package com.myth.arch.mvvm.view

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment

class MythFragment : Fragment(), MythView {

    private val viewProvider = MythViewProvider()

    override fun getBaseBikeViewProvider(): MythViewProvider {
        return viewProvider
    }

    override fun getContext(): Context? {
        return activity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this as MythView).init(this)
    }

}