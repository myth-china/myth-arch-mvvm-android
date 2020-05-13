package com.myth.arch.mvvm2.exception

class MythIllegalAccessException(msg: String) :
    IllegalAccessException("${MythExceptionConst.prefix} $msg")