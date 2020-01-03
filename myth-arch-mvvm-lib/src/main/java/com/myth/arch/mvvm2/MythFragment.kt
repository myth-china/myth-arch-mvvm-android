package com.myth.arch.mvvm2

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner

class MythFragment : Fragment(), MythView {

    override fun getContext2(): Context? {
        return context
    }

    override fun getFragmentManager2(): FragmentManager? {
        return fragmentManager
    }

    override fun getLifeCycleOwner(): LifecycleOwner {
        return this
    }
}
