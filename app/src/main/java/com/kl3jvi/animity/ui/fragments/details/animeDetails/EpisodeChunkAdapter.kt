package com.kl3jvi.animity.ui.fragments.details.animeDetails

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel

class EpisodeChunkAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle,
    episodeChunks: List<List<EpisodeModel>>,
    private val animeDetails: AniListMedia,
    private val desiredPosition: Int,
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    private var episodeChunks: List<List<EpisodeModel>> = emptyList()

    init {
        setData(episodeChunks)
    }

    override fun getItemCount(): Int = episodeChunks.size

    override fun createFragment(position: Int): Fragment {
        return EpisodeContainer.newInstance(
            episodeChunks[position],
            position,
            animeDetails,
            desiredPosition,
        )
    }

    fun setData(newEpisodeChunks: List<List<EpisodeModel>>) {
        val diffResult = DiffUtil.calculateDiff(
            EpisodeChunkDiffCallback(
                episodeChunks,
                newEpisodeChunks,
            ),
        )

        episodeChunks = newEpisodeChunks
        diffResult.dispatchUpdatesTo(this)
    }
}
