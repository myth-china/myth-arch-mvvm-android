package com.myth.mama

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.myth.arch.mvvm3.*
import com.myth.arch.mvvm3.vmext.showLoading
import com.myth.arch.mvvm3.vmext.startActivity
import com.myth.arch.mvvm3.vmext.startActivityForResult
import com.myth.arch.mvvm3.vmext.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), MythView {
    private val viewModel by lazy { viewModelOf(MainViewModel::class.java) }
    private val funcList = arrayListOf(
        FuncItem("Use Activity Block") {
            viewModel.remoteUseActivity()
        },
        FuncItem("Show Loading") {
            viewModel.showLoading(true)
        },
        FuncItem("Show Toast") {
            viewModel.toast("Toast in ViewModel")
        },
        FuncItem("Start Activity") {
            viewModel.startActivity(SecondActivity::class.java)
        },
        FuncItem("Start Activity For Result") {
            viewModel.startActivityForResult(
                SecondActivity::class.java,
                Bundle().apply { putString("data", "Data From Parent Activity") },
                0x11
            )
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        rv_func_list.layoutManager = LinearLayoutManager(this)
        rv_func_list.adapter = MainAdapter(funcList)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x11 && resultCode == 0x11) {
            data?.getStringExtra("data")?.let { viewModel.toast(it) }
        }
    }
}

data class FuncItem(val name: String, val func: () -> Unit)