package com.myth.arch.mvvm3

import androidx.lifecycle.LiveData

typealias MythViewModelExt<T> = (MythView, LiveData<T>) -> Unit