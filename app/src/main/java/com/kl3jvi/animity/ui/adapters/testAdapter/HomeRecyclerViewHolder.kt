package com.kl3jvi.animity.ui.adapters.testAdapter

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.chip.Chip
import com.kl3jvi.animity.databinding.ItemHorizontalRecyclerBinding
import com.kl3jvi.animity.databinding.ItemTitleBinding
import com.kl3jvi.animity.databinding.ItemTodaySelectionBinding
import com.kl3jvi.animity.ui.adapters.AnimeCardAdapter

import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Constants.Companion.getColor


sealed class HomeRecyclerViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class TitleViewHolder(private val binding: ItemTitleBinding) : HomeRecyclerViewHolder(binding) {
        fun bindTitle(title: HomeRecyclerViewItem.Title) {
            binding.contentTitle.text = title.title
        }
    }

    class HorizontalViewHolder(
        val binding: ItemHorizontalRecyclerBinding,
        sharedViewPool: RecyclerView.RecycledViewPool
    ) : HomeRecyclerViewHolder(binding) {
        private val horizontalAdapter = AnimeCardAdapter()

        init {
            binding.innerRv.setRecycledViewPool(sharedViewPool)
            binding.root.isNestedScrollingEnabled = true
            binding.innerRv.adapter = horizontalAdapter
        }

        fun bindList(animeData: HomeRecyclerViewItem.HorizontalAnimeWrapper) {
            horizontalAdapter.submitList(animeData.animeList)
        }
    }

    class VerticalViewHolder(val binding: ItemTodaySelectionBinding) :
        HomeRecyclerViewHolder(binding) {
//        private val verticalAdapter = CustomVerticalAdapter()

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

        private fun navigateToDetails(
            animeDetails: HomeRecyclerViewItem.Anime,
            view: View
        ) {
            try {
                /**
                 * playButtonFlag = shows little play button on top of anime card view layout
                 * If playButtonFlag is false we are at profile else we are at home!
                 */
//                val direction = if (true)
//                    HomeFragmentDirections.actionNavigationHomeToDetailsFragment(animeDetails)
//                else ProfileFragmentDirections.actionNavigationProfileToNavigationDetails(
//                    animeDetails
//                )
//                view.findNavController().navigate(direction)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }

        fun bindVerticalAnime(animeList: HomeRecyclerViewItem.Anime) {

            binding.animeInfo = animeList
            binding.chipGroup.removeAllViews()
            val arrayOfGenres = animeList.genreList
            arrayOfGenres?.let { genres ->
                genres.forEach { data ->
                    if (data.genreName.isNotBlank()) {
                        val chip = Chip(binding.root.context)
                        chip.apply {
                            text = data.genreName
                            setTextColor(Color.WHITE)
                            chipStrokeColor = getColor()
                            chipStrokeWidth = 3f
                            chipBackgroundColor = Constants.getVerticalAdapterBackgroundColor()
                        }
                        binding.chipGroup.addView(chip)
                    } else
                        binding.chipGroup.removeAllViews()
                }
            }
            binding.executePendingBindings()
        }
    }
}

