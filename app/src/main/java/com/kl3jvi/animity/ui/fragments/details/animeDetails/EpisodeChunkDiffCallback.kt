package com.kl3jvi.animity.ui.fragments.details.animeDetails

import androidx.recyclerview.widget.DiffUtil
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel

class EpisodeChunkDiffCallback(
    private val oldChunks: List<List<EpisodeModel>>,
    private val newChunks: List<List<EpisodeModel>>
) : DiffUtil.Callback() {

    override fun getOldListSize() = oldChunks.size

    override fun getNewListSize() = newChunks.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldChunk = oldChunks[oldItemPosition]
        val newChunk = newChunks[newItemPosition]

        // Compare sizes
        if (oldChunk.size != newChunk.size) return false

        // Compare all elements
        for (i in oldChunk.indices) {
            if (oldChunk[i].episodeNumber != newChunk[i].episodeNumber) return false
            if (oldChunk[i].percentage != newChunk[i].percentage) return false
        }

        return true
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldChunk = oldChunks[oldItemPosition]
        val newChunk = newChunks[newItemPosition]
        // Compare all episodes, not just the first one
        return oldChunk.size == newChunk.size &&
            oldChunk.zip(newChunk).all { (oldEpisode, newEpisode) ->
                oldEpisode.percentage == newEpisode.percentage &&
                    oldEpisode.episodeName == newEpisode.episodeName &&
                    oldEpisode.episodeNumber == newEpisode.episodeNumber
            }
    }
}
