package com.kl3jvi.animity.ui.fragments.details.animeDetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel

class EpisodeChunkAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    private var episodeChunks: List<List<EpisodeModel>>,
    private val animeDetails: AniListMedia
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int = episodeChunks.size

    override fun createFragment(position: Int): Fragment {
        return EpisodeContainer.newInstance(episodeChunks[position], animeDetails)
    }

    fun setData(newEpisodeChunks: List<List<EpisodeModel>>) {
        episodeChunks = newEpisodeChunks
        notifyDataSetChanged()
    }
}
