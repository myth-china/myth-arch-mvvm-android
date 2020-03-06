package com.myth.arch.mvvm2.ext

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.myth.arch.event.Event2
import com.myth.arch.event.EventObserver2
import com.myth.arch.mvvm2.MythView
import com.myth.arch.mvvm2.MythViewModel

class FinishResultExt : MythViewModelExt<Event2<Int?, Bundle?>>() {

    companion object {
        const val finishResult = "finishResult"

        fun finishResult(viewModel: MythViewModel, data: Event2<Int?, Bundle?>) {
            @Suppress("UNCHECKED_CAST")
            val scaffold =
                viewModel.extMap[finishResult] as? MythViewModelExt<Event2<Int?, Bundle?>>
            scaffold?.getData()?.postValue(data)
        }
    }

    override fun setup(view: MythView) {
        getData().observe(
            view.getLifeCycleOwner(),
            EventObserver2<Int?, Bundle?> { resultCode, data ->
                resultCode ?: return@EventObserver2
                val intent = Intent().apply {
                    data?.let { putExtras(it) }
                }
                if (view is Activity) {
                    view.setResult(resultCode, intent)
                }
            })
    }
}

fun MythViewModel.setResult(resultCode: Int?, bundle: Bundle?) {
    FinishResultExt.finishResult(this, Event2(resultCode, bundle))
}

