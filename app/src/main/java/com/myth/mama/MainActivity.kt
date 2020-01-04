package com.myth.mama

import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.myth.arch.mvvm2.MythActivity
import com.myth.arch.mvvm2.ext.fire
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : MythActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val viewModel: MainViewModel =
            ViewModelProviders.of(this).get(MainViewModel::class.java).fire(this)

        btn.setOnClickListener {
            viewModel.openSecondPage()
        }
    }
}
