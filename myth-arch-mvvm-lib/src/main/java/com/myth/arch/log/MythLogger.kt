package com.myth.arch.log

import android.util.Log

object MythLogger {

    var DEBUG = false

    fun d(tag: String, msg: String) {
        if (DEBUG) {
            Log.d(tag, msg)
        }
    }
}