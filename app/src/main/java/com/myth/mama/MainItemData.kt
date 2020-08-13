package com.myth.mama

import android.view.View

typealias  Click = (View) -> Unit

data class MainItemData(val name: String, val click: Click)