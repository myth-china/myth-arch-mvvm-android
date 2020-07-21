package com.myth.arch.exception

class MythIllegalArgumentException(msg: String) :
    IllegalArgumentException("${MythExceptionConst.prefix} $msg")