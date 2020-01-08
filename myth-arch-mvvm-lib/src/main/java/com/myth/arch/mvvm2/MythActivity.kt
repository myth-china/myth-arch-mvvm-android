package com.myth.arch.mvvm2

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

open class MythActivity : AppCompatActivity(), MythView {

    override fun getContext2(): Context? {
        return this
    }

    override fun getFragmentManager2(): FragmentManager? {
        return supportFragmentManager
    }

    override fun getLifeCycleOwner(): LifecycleOwner {
        return this
    }
}