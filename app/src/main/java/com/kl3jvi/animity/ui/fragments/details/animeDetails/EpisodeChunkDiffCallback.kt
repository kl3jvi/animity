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
        return oldChunk.size == newChunk.size && oldChunk[0].episodeName == newChunk[0].episodeName
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldChunk = oldChunks[oldItemPosition]
        val newChunk = newChunks[newItemPosition]
        return oldChunk == newChunk
    }
}
