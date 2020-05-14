package com.myth.mama

import android.content.Intent
import android.os.Bundle
import com.myth.arch.mvvm2.MythActivity
import com.myth.arch.mvvm2.ext.toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MythActivity() {
    private val viewModel by lazy { viewModelOf(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            viewModel.openSecondPageForResult()
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.toast("OnResumed toast with ext")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x11 && resultCode == 0x11) {
            btn.text = data?.getStringExtra("data")
        }
    }
}
