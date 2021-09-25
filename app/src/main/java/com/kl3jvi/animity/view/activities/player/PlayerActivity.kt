package com.kl3jvi.animity.view.activities.player

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kl3jvi.animity.databinding.ActivityPlayerBinding
import com.kl3jvi.animity.model.entities.Content
import com.kl3jvi.animity.model.network.ApiHelper
import com.kl3jvi.animity.model.network.RetrofitBuilder

private lateinit var viewModel: PlayerViewModel
private var episodeNumber: String? = ""

class PlayerActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPlayerBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlayerBinding.inflate(layoutInflater)
        setContentView(binding.root)
        viewModel = ViewModelProvider(
            this,
            PlayerViewModelFactory(ApiHelper(RetrofitBuilder.apiService))
        ).get(PlayerViewModel::class.java)
        getExtra(intent)
    }

    private fun getExtra(intent: Intent?) {
        val url = intent?.extras?.getString("episodeUrl")
        episodeNumber = intent?.extras?.getString("episodeNumber")
        viewModel.updateEpisodeContent(
            Content(
                episodeUrl = url,
                episodeName = "(" + episodeNumber!! + ")",
                url = ""
            )
        )
        viewModel.fetchEpisodeMediaUrl() // FIXME: 25.9.21  
    }


    fun fetchEpisodeMediaUrl(){
        viewModel.liveContent.value?.episodeUrl?.let {
            println(it)
        }
    }
}