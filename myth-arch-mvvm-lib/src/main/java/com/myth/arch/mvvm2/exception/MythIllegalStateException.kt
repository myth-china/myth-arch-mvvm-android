package com.myth.arch.mvvm2.exception

class MythIllegalStateException(msg: String) :
    IllegalStateException("${MythExceptionConst.prefix} $msg")