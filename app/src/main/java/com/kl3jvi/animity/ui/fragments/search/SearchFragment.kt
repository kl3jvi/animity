package com.kl3jvi.animity.ui.fragments.search

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.chip.Chip
import com.kl3jvi.animity.R
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.databinding.FragmentSearchBinding
import com.kl3jvi.animity.utils.collectLatest
import com.kl3jvi.animity.utils.dismissKeyboard
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {

    private val viewModel: SearchViewModel by viewModels()
    private lateinit var pagingController: PagingSearchController
    private var binding: FragmentSearchBinding? = null

    @Inject
    lateinit var analytics: Analytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)
        pagingController = PagingSearchController(analytics)
        initViews()
        observeViewModel()
        setupSortChips()
    }

    /**
     * It sets up the search view and recycler view.
     */
    private fun initViews() {
        binding?.apply {
            searchRecycler.setController(pagingController)
            mainSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String): Boolean {
                    dismissKeyboard(binding?.mainSearch)
                    return false
                }

                override fun onQueryTextChange(query: String): Boolean {
                    viewModel.onSearchQueryChanged(query)
                    return false
                }
            })
        }
    }

    private fun setupSortChips() {
        binding?.apply {
            val chipMap = mapOf(
                sortTitle to SortType.TITLE,
                sortDate to SortType.START_DATE,
                sortPopularity to SortType.POPULARITY,
                sortAverageScore to SortType.AVERAGE_SCORE,
                sortTrending to SortType.TRENDING,
                sortFavorites to SortType.FAVOURITES,
                sortEpisodes to SortType.EPISODES
            )

            val previousChipStates =
                mutableMapOf<Chip, Triple<ColorStateList?, ColorStateList?, Float>>()

            for ((chip, sortType) in chipMap) {
                chip.setOnClickListener {
                    val isSelected = !chip.isSelected
                    chip.isSelected = isSelected

                    if (isSelected) {
                        viewModel.toggleSortType(sortType)
                        previousChipStates[chip] = Triple(
                            chip.chipStrokeColor,
                            chip.chipBackgroundColor,
                            chip.chipStrokeWidth
                        )

                        chip.chipStrokeColor = ColorStateList.valueOf(Color.parseColor("#FFFFFF"))
                        chip.chipBackgroundColor =
                            ColorStateList.valueOf(Color.parseColor("#43C59E"))
                        chip.chipStrokeWidth = 3f
                    } else {
                        viewModel.toggleSortType(sortType)
                        val previousState = previousChipStates[chip]
                        previousState?.let { state ->
                            chip.chipStrokeColor = state.first
                            chip.chipBackgroundColor = state.second
                            chip.chipStrokeWidth = state.third
                        }
                        previousChipStates.remove(chip)
                    }
                }
            }
        }
    }

    private fun animateExpand(view: View) {
        val initialHeight = 0
        val targetHeight = view.height

        val animator = ValueAnimator.ofInt(initialHeight, targetHeight)
        animator.duration = ANIMATION_DURATION
        animator.addUpdateListener { animation ->
            val animatedValue = animation.animatedValue as Int
            view.layoutParams.height = animatedValue
            view.requestLayout()
        }
        animator.start()
    }

    private fun animateDropdown(view: View, isShow: Boolean) {
        val initialY = if (isShow) -view.height.toFloat() else 0f
        val finalY = if (isShow) 0f else -view.height.toFloat()

        val animator = ObjectAnimator.ofFloat(view, View.TRANSLATION_Y, initialY, finalY)

        animator.duration = ANIMATION_DURATION
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.start()
    }

    private fun toggleViewVisibilityWithAnimation(view: View, isShow: Boolean) {
        if (isShow) {
            view.visibility = View.VISIBLE
            animateDropdown(view, isShow)
        } else {
            animateDropdown(view, isShow)
            view.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // letting go of the resources to avoid memory leak.
        binding = null
    }

    override fun onResume() {
        super.onResume()
        analytics.logCurrentScreen("Search")
    }

    override fun onPause() {
        dismissKeyboard(binding?.mainSearch)
        super.onPause()
    }

    private fun observeViewModel() {
        pagingController.addInterceptor {
            toggleViewVisibilityWithAnimation(binding?.sortView!!, it.size > 0)
        }
        collectLatest(viewModel.searchList) { animeData ->
            pagingController.submitData(animeData)
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 1000L
    }
}
