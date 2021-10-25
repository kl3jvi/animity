package com.kl3jvi.animity.view.activities.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.kl3jvi.animity.databinding.ActivityPlayerBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlayerActivity : AppCompatActivity() {

    private val viewBinding by lazy(LazyThreadSafetyMode.NONE) {
        ActivityPlayerBinding.inflate(layoutInflater)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(viewBinding.root)

    }
}




