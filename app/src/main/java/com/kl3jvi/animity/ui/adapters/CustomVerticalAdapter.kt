package com.kl3jvi.animity.ui.adapters

import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.chip.Chip
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.databinding.ItemTodaySelectionBinding
import com.kl3jvi.animity.ui.fragments.home.HomeFragmentDirections
import com.kl3jvi.animity.ui.fragments.profile.ProfileFragmentDirections
import com.kl3jvi.animity.utils.Constants.Companion.getVerticalAdapterBackgroundColor

class CustomVerticalAdapter(var playButtonFlag: Boolean = true) :
    ListAdapter<AnimeMetaModel, CustomVerticalAdapter.ViewHolder>(MainDiffUtil<AnimeMetaModel>()) {

    inner class ViewHolder(val binding: ItemTodaySelectionBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            binding.setClickListener { view ->
                binding.animeInfo?.let { animeDetails ->
                    navigateToDetails(
                        animeDetails,
                        view = view
                    )
                }
            }
        }

        private fun navigateToDetails(
            animeDetails: AnimeMetaModel,
            view: View
        ) {
            try {
                /**
                 * playButtonFlag = shows little play button on top of anime card view layout
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

        fun bindAnimeItem(animeMetaModel: AnimeMetaModel) {
            binding.animeInfo = animeMetaModel


            binding.chipGroup.removeAllViews()
            val arrayOfGenres = animeMetaModel.genreList
            arrayOfGenres?.let { genres ->
                genres.forEach { data ->
                    if (data.genreName.isNotBlank()) {
                        val chip = Chip(binding.root.context)
                        chip.apply {
                            text = data.genreName
                            setTextColor(Color.WHITE)
                            chipStrokeColor = getColor()
                            chipStrokeWidth = 3f
                            chipBackgroundColor = getVerticalAdapterBackgroundColor()
                        }
                        binding.chipGroup.addView(chip)
                    } else
                        binding.chipGroup.removeAllViews()
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding: ItemTodaySelectionBinding =
            ItemTodaySelectionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bindAnimeItem(getItem(position))


    private fun getColor(): ColorStateList {
        val color: Int = Color.argb(255, 4, 138, 129)
        return ColorStateList.valueOf(color)
    }

    override fun getItemCount() = currentList.size

}
