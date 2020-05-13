package com.myth.mama

import android.content.Intent
import android.os.Bundle
import com.myth.arch.mvvm2.MythActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MythActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel = viewModelOf(MainViewModel::class.java)

        btn.setOnClickListener {
            viewModel.openSecondPageForResult()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 0x11 && resultCode == 0x11) {
            btn.text = data?.getStringExtra("data")
        }
    }
}
