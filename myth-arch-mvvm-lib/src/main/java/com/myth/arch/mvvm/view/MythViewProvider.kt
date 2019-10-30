package com.myth.arch.mvvm.view

import android.app.ProgressDialog
import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.myth.arch.mvvm.MythViewModel
import com.myth.arch.mvvm.coroutine.CoroutineMain

class MythViewProvider {
    var activity: AppCompatActivity? = null
    var fragment: Fragment? = null
    var context: Context? = null
    var viewModel: MythViewModel? = null

    val coroutineMain by lazy { CoroutineMain() }
    val loadingDialog by lazy { context?.let { ProgressDialog(context) } }
}