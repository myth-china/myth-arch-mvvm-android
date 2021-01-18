package com.myth.mama

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.myth.arch.mvvm3.MythView
import com.myth.arch.mvvm3.toast
import kotlinx.android.synthetic.main.activity_second.*

class SecondActivity : AppCompatActivity(), MythView {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_second)

        val viewModel = viewModelOf(SecondViewModel::class.java)

        viewModel.toast("Second page")

        tv.text = intent.getStringExtra("data")

        b_finish.setOnClickListener {
            setResult(0x11, Intent().apply {
                putExtra("data", "back from second page")
            })
            finish()
        }
    }
}
