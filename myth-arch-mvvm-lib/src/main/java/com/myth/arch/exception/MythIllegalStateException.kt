package com.myth.arch.exception

class MythIllegalStateException(msg: String) :
    IllegalStateException("${MythExceptionConst.prefix} $msg")