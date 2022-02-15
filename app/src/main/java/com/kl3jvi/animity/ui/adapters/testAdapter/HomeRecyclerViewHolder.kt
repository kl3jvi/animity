package com.kl3jvi.animity.ui.adapters.testAdapter

import android.graphics.Color
import android.view.View
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import com.google.android.material.chip.Chip
import com.kl3jvi.animity.databinding.ItemHorizontalRecyclerBinding
import com.kl3jvi.animity.databinding.ItemTitleBinding
import com.kl3jvi.animity.databinding.ItemTodaySelectionBinding
import com.kl3jvi.animity.ui.adapters.TestAdapter
import com.kl3jvi.animity.ui.fragments.home.HomeFragmentDirections
import com.kl3jvi.animity.ui.fragments.profile.ProfileFragmentDirections
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Constants.Companion.getColor


sealed class HomeRecyclerViewHolder(binding: ViewBinding) : RecyclerView.ViewHolder(binding.root) {

    class TitleViewHolder(private val binding: ItemTitleBinding) : HomeRecyclerViewHolder(binding) {
        fun bind(title: HomeRecyclerViewItem.Title) {
            binding.contentTitle.text = title.title
        }
    }

    class HorizontalViewHolder(
        private val binding: ItemHorizontalRecyclerBinding
    ) : HomeRecyclerViewHolder(binding) {
        private val horizontalAdapter = TestAdapter()

        init {
            val linearLayoutManager =
                LinearLayoutManager(binding.root.context, RecyclerView.HORIZONTAL, false)
            binding.root.layoutManager = linearLayoutManager
            binding.root.adapter = horizontalAdapter
        }

        fun bind(animeData: List<HomeRecyclerViewItem.Anime>) {
            horizontalAdapter.submitList(animeData)
        }
    }

    class VerticalViewHolder(val binding: ItemTodaySelectionBinding) :
        HomeRecyclerViewHolder(binding) {
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
            animeDetails: HomeRecyclerViewItem.AnimeVertical,
            view: View
        ) {
            try {
                /**
                 * playButtonFlag = shows little play button on top of anime card view layout
                 * If playButtonFlag is false we are at profile else we are at home!
                 */
                val direction = if (true)
                    HomeFragmentDirections.actionNavigationHomeToDetailsFragment(animeDetails)
                else ProfileFragmentDirections.actionNavigationProfileToNavigationDetails(
                    animeDetails
                )
                view.findNavController().navigate(direction)
            } catch (e: IllegalArgumentException) {
                e.printStackTrace()
            }
        }

        fun bindAnimeItem(animeMetaModel: HomeRecyclerViewItem.AnimeVertical) {
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
                            chipBackgroundColor = Constants.getVerticalAdapterBackgroundColor()
                        }
                        binding.chipGroup.addView(chip)
                    } else
                        binding.chipGroup.removeAllViews()
                }
            }
        }
    }

}
