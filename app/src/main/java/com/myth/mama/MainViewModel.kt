package com.myth.mama

import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.myth.arch.mvvm3.MythViewModel
import com.myth.arch.mvvm3.vmext.*

class MainViewModel : ViewModel(), MythViewModel {

    override fun onFired(data: Bundle) {

    }

    /**
     * 在ViewModel中使用实Fragment实例
     */
    fun remoteUseFragment() {
        useFragment {
            //此处是ViewModel绑定的Fragment实例，若绑定的不是Fragment，此代码块不会被调用
            Toast.makeText(it.context, "Toast in useFragment Block", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 在ViewModel中使用Activity实例
     */
    fun remoteUseActivity() {
        useActivity {
            //此处是ViewModel绑定的Activity实例，若绑定的不是Activity，此代码块不会被调用
            Toast.makeText(it, "Toast in useActivity Block", Toast.LENGTH_LONG).show()
        }
    }

    /**
     * 在ViewModel中显示一个toast提示
     */
    fun remoteToast(text: String) {
        toast(text)
    }

    /**
     * 在ViewModel中启动新的Activity
     */
    fun remoteStartActivity() {
        startActivity(SecondActivity::class.java, Bundle().apply {
            putString("data", "from main page")
        })
    }

    /**
     * 在ViewModel中启动新的Activity，并获取结果
     */
    fun remoteStartActivityForResult() {
        startActivityForResult(
            SecondActivity::class.java,
            Bundle().apply {
                putString("data", "from main page")
            },
            0x11
        )
    }

    /**
     * 在ViewModel中调用Activity的finish()
     */
    fun remoteFinish() {
        finish()
    }

    /**
     * 在ViewModel中调用Fragment的popBackStack()
     */
    fun remotePopBackStack() {
        popBackStack()
    }
}