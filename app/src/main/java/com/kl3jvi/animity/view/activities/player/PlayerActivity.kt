package com.kl3jvi.animity.view.activities.player

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.kl3jvi.animity.databinding.ActivityPlayerBinding

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}