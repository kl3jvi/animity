package com.kl3jvi.animity.view.activities.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

import com.kl3jvi.animity.databinding.ActivityPlayerBinding
import dagger.hilt.android.AndroidEntryPoint


private lateinit var viewModel: PlayerViewModel
private var episodeNumber: String? = ""
private var episodeUrlGlobal: String? = ""

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




