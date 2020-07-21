package com.myth.arch.exception

class MythIllegalAccessException(msg: String) :
    IllegalAccessException("${MythExceptionConst.prefix} $msg")