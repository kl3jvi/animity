package com.kl3jvi.animity.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.databinding.ItemFavoriteAnimeBinding
import com.kl3jvi.animity.ui.fragments.favorites.FavoritesFragmentDirections

class CustomFavoriteAdapter : ListAdapter<AnimeMetaModel, CustomFavoriteAdapter.ViewHolder>(
    MainDiffUtil()
) {
//    RecyclerView.Adapter<CustomFavoriteAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemFavoriteAnimeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { view ->
                binding.animeInfo?.let {
                    navigateToDetails(it, view)
                }
            }
        }

        private fun navigateToDetails(animeDetails: AnimeMetaModel, view: View) {
            try {
                val direction =
                    FavoritesFragmentDirections.actionNavigationFavoritesToNavigationDetails(
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
            binding.executePendingBindings()
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): CustomFavoriteAdapter.ViewHolder {
        val binding: ItemFavoriteAnimeBinding =
            ItemFavoriteAnimeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CustomFavoriteAdapter.ViewHolder, position: Int) =
        holder.bindAnimeInfo(getItem(position))

    override fun getItemCount(): Int = currentList.size

}



