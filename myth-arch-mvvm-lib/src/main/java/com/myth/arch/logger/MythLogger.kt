package com.myth.arch.logger

import android.util.Log

object MythLogger {
    private const val prefix = "Myth-> "

    fun i(tag: String, msg: String?) {
        Log.i("$prefix$tag", msg ?: "Null msg!!")
    }

    fun d(tag: String, msg: String?) {
        Log.d("$prefix$tag", msg ?: "Null msg!!")
    }

    fun w(tag: String, msg: String?) {
        Log.w("$prefix$tag", msg ?: "Null msg!!")
    }

    fun e(tag: String, msg: String?) {
        Log.e("$prefix$tag", msg ?: "Null msg!!")
    }
}