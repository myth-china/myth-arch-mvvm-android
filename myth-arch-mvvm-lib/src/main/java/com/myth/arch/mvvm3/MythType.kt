package com.myth.arch.mvvm3

import androidx.lifecycle.LiveData
import com.myth.arch.mvvm2.MythView

typealias MythViewModelExt<T> = (MythView, LiveData<T>) -> Unit