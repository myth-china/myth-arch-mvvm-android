package com.myth.arch.mvvm.view

import android.content.Context
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment

open class MythDialogFragment : DialogFragment(), MythView {

    private val viewProvider = MythViewProvider()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        (this as MythView).init(this)
    }

    override fun onStart() {
        super.onStart()
        val dialogWidth = resources.displayMetrics.widthPixels
        dialog.window?.setLayout(dialogWidth, ViewGroup.LayoutParams.MATCH_PARENT)
        dialog.window?.setBackgroundDrawableResource(android.R.color.transparent)
    }

    override fun getBaseBikeViewProvider(): MythViewProvider {
        return viewProvider
    }

    override fun getContext(): Context? {
        return activity
    }
}