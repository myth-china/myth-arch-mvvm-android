package com.myth.arch.mvvm3

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.myth.arch.exception.MythIllegalAccessException
import com.myth.arch.logger.MythLogger

/**
 * Use Block
 */
fun MythViewModel.useFragment(callback: (Fragment) -> Unit) {
    val name = "useFragment"

    easyUseExt(name, callback, false) { view, innerData ->
        if (view is Fragment) {
            innerData?.invoke(view)
        }
    }
}

fun MythViewModel.useActivity(callback: (AppCompatActivity) -> Unit) {
    val name = "useActivity"
    easyUseExt(name, callback, false) { view, _ ->
        if (view is Fragment) {
            return@easyUseExt
        }
        view.getActivity2()?.let {
            callback(it as AppCompatActivity)
        }
    }
}

/**
 * Toast
 */
fun MythViewModel.toast(text: String) {
    val name = "toast"
    easyUseExt(name, text, true) { view, innerText ->
        val context = view.getContext2() ?: return@easyUseExt
        Toast.makeText(context, innerText, Toast.LENGTH_LONG).show()
    }
}

/**
 * Navigator
 */
fun MythViewModel.startActivity(cls: Class<out AppCompatActivity>, args: Bundle? = null) {
    val name = "navigator"

    easyUseExt(name, cls to args, false) { view, innerData ->
        innerData ?: return@easyUseExt
        val innerCls = innerData.first
        val innerArgs = innerData.second
        val context = view.getContext2() ?: return@easyUseExt
        context.startActivity(Intent(context, innerCls).apply {
            innerArgs?.run {
                putExtras(this)
            }
        })
    }
}

fun MythViewModel.startActivityForResult(
        cls: Class<out AppCompatActivity>,
        args: Bundle? = null,
        requestCode: Int
) {

    MythLogger.d("startActivityForResult", "called")
    val name = "navigatorForResult"
    easyUseExt(name, cls to (args to requestCode), true) { view, innerData ->

        MythLogger.d("startActivityForResult", "exec ${innerData.toString()}")

        innerData ?: return@easyUseExt

        val innerCls = innerData.first
        val innerArgs = innerData.second.first
        val innerRequestCode = innerData.second.second

        when (view) {
            is FragmentActivity -> {
                view.startActivityForResult(Intent(view, innerCls).apply {
                    innerArgs?.run {
                        putExtras(this)
                    }
                }, innerRequestCode)
            }
            is Fragment -> {
                view.startActivityForResult(Intent(view.context, innerCls).apply {
                    innerArgs?.run {
                        putExtras(this)
                    }
                }, innerRequestCode)
            }
            else -> {
                throw MythIllegalAccessException("Can't startActivityForResult, the view is not a FragmentActivity or Fragment!")
            }
        }
    }
}

fun MythViewModel.finish() {
    val name = "finish"
    easyUseExt(name, null, true) { view, _ ->
        when (view) {
            is FragmentActivity -> {
                view.finish()
            }
            is Fragment -> {
                view.activity?.finish()
            }
            else -> {
                throw MythIllegalAccessException("Can't startActivityForResult, the view is not a FragmentActivity or Fragment!")
            }
        }
    }
}


fun MythViewModel.popBackStack() {
    val name = "popBackStack"
    easyUseExt(name, null, true) { view, _ ->
        when (view) {
            is FragmentActivity -> {
                view.supportFragmentManager.popBackStack()
            }
            is Fragment -> {
                view.parentFragmentManager.popBackStack()
            }
            else -> {
                throw IllegalStateException("Can't startActivityForResult, the view is not a FragmentActivity or Fragment!")
            }
        }
    }
}