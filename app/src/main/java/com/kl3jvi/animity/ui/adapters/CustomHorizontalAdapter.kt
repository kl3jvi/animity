package com.kl3jvi.animity.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kl3jvi.animity.databinding.ItemCardAnimeBinding
import com.kl3jvi.animity.model.AnimeMetaModel
import com.kl3jvi.animity.ui.fragments.home.HomeFragmentDirections

class CustomHorizontalAdapter : ListAdapter<AnimeMetaModel, CustomHorizontalAdapter.AnimeViewHolder>(
    AnimeDiffCallback()
) {

    inner class AnimeViewHolder constructor(
        private val binding: ItemCardAnimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { view ->
                binding.animeInfo?.let{
                    navigateToDetails(it, view)
                }
            }
        }

        private fun navigateToDetails(animeDetails: AnimeMetaModel, view: View) {
            val direction =
                HomeFragmentDirections.actionNavigationHomeToDetailsFragment(animeDetails)
            view.findNavController().navigate(direction)
        }

        fun bindAnimeInfo(animeInfo: AnimeMetaModel) {
            binding.animeInfo = animeInfo
            binding.isVisible = !animeInfo.episodeNumber.isNullOrEmpty()
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {
        val binding: ItemCardAnimeBinding =
            ItemCardAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) =
        holder.bindAnimeInfo(getItem(position))

    private class AnimeDiffCallback : DiffUtil.ItemCallback<AnimeMetaModel>() {

        override fun areItemsTheSame(
            oldItem: AnimeMetaModel,
            newItem: AnimeMetaModel
        ): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }

        override fun areContentsTheSame(
            oldItem: AnimeMetaModel,
            newItem: AnimeMetaModel
        ): Boolean {
            return oldItem.imageUrl == newItem.imageUrl
        }
    }
}
