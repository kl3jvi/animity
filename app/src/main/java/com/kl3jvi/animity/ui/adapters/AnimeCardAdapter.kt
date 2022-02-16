package com.kl3jvi.animity.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.databinding.ItemCardAnimeBinding
import com.kl3jvi.animity.ui.fragments.home.HomeFragmentDirections
import com.kl3jvi.animity.ui.fragments.profile.ProfileFragmentDirections

class AnimeCardAdapter(var isHomeFragment: Boolean = true) :
    ListAdapter<AnimeMetaModel, AnimeCardAdapter.AnimeViewHolder>(MainDiffUtil<AnimeMetaModel>()) {

    inner class AnimeViewHolder(
        private val binding: ItemCardAnimeBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { view ->
                binding.animeInfo?.let { animeDetails ->
                    navigateToDetails(
                        animeDetails,
                        view
                    )
                }
            }
        }

        private fun navigateToDetails(animeDetails: AnimeMetaModel, view: View) {
            try {
                val direction = if (isHomeFragment)
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
//            binding.playLogo.isVisible = true
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AnimeViewHolder {

        val binding: ItemCardAnimeBinding =
            ItemCardAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AnimeViewHolder(binding)
    }

    override fun getItemCount(): Int = currentList.size

    override fun onBindViewHolder(holder: AnimeViewHolder, position: Int) =
        holder.bindAnimeInfo(getItem(position))


}