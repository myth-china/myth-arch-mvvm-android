package com.myth.arch.mvvm2.exception

class MythIllegalArgumentException(msg: String) :
    IllegalArgumentException("${MythExceptionConst.prefix} $msg")