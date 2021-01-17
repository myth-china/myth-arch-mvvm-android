package com.myth.mama

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.myth.arch.mvvm3.MythView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), MythView {
    private val viewModel by lazy { viewModelOf(MainViewModel::class.java) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        btn.setOnClickListener {
            viewModel.remoteStartActivityForResult()
        }

        lifecycleScope.launch { }
    }

    override fun onResume() {
        super.onResume()
        viewModel.remoteToast("OnResumed toast with ext")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x11 && resultCode == 0x11) {
            btn.text = data?.getStringExtra("data")
        }
    }
}