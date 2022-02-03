package com.kl3jvi.animity.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.databinding.ItemCardAnimeBinding
import com.kl3jvi.animity.ui.fragments.home.HomeFragmentDirections
import com.kl3jvi.animity.ui.fragments.profile.ProfileFragmentDirections

class CustomHorizontalAdapter(val playButtonFlag: Boolean = true) :
    ListAdapter<AnimeMetaModel, CustomHorizontalAdapter.AnimeViewHolder>(AnimeDiffCallback()) {

    inner class AnimeViewHolder constructor(
        private val binding: ItemCardAnimeBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { view ->
                binding.animeInfo?.let {
                    navigateToDetails(it, view)
                }
            }
        }

        private fun navigateToDetails(animeDetails: AnimeMetaModel, view: View) {
            try {
                /**
                 * If playButtonFlag is false we are at profile else we are at home!
                 */
                val direction = if (playButtonFlag)
                    HomeFragmentDirections.actionNavigationHomeToDetailsFragment(animeDetails)
                else ProfileFragmentDirections.actionNavigationProfileToNavigationDetails(
                    animeDetails
                )
                view.findNavController().navigate(direction)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }

        fun bindAnimeInfo(animeInfo: AnimeMetaModel) {
            binding.animeInfo = animeInfo
            binding.isVisible = !animeInfo.episodeNumber.isNullOrEmpty()
            binding.playLogo.isVisible = playButtonFlag
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

    override fun getItemCount(): Int = currentList.size

    private class AnimeDiffCallback : DiffUtil.ItemCallback<AnimeMetaModel>() {

        override fun areItemsTheSame(
            oldItem: AnimeMetaModel,
            newItem: AnimeMetaModel
        ): Boolean {
            return oldItem.toString() == newItem.toString()
        }

        override fun areContentsTheSame(
            oldItem: AnimeMetaModel,
            newItem: AnimeMetaModel
        ): Boolean {
            return oldItem == newItem
        }
    }
}
