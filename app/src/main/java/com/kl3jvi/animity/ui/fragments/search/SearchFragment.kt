package com.kl3jvi.animity.ui.fragments.search

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
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

    private fun expandView(view: View) {
        if (view.layoutParams.height != 0) return

        view.measure(
            View.MeasureSpec.makeMeasureSpec((view.parent as View).width, View.MeasureSpec.EXACTLY),
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
        )
        val targetHeight = view.measuredHeight

        val valueAnimator = ValueAnimator.ofInt(0, targetHeight)
        valueAnimator.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Int
            view.layoutParams.height = animatedValue
            view.requestLayout()
        }

        valueAnimator.duration = ANIMATION_DURATION
        valueAnimator.start()
    }

    private fun collapseView(view: View) {
        if (view.layoutParams.height == 0) return

        val initialHeight = view.measuredHeight

        val valueAnimator = ValueAnimator.ofInt(initialHeight, 0)
        valueAnimator.addUpdateListener { animator ->
            val animatedValue = animator.animatedValue as Int
            view.layoutParams.height = animatedValue
            view.requestLayout()
        }

        valueAnimator.duration = ANIMATION_DURATION
        valueAnimator.start()
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
        val sortView = binding?.sortView!!

        collapseView(sortView)

        binding?.sortView?.viewTreeObserver?.addOnGlobalLayoutListener(object :
                ViewTreeObserver.OnGlobalLayoutListener {
                override fun onGlobalLayout() {
                    binding?.sortView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                    pagingController.addInterceptor {
                        if (it.size == 0) {
                            collapseView(sortView)
                        } else {
                            expandView(sortView)
                        }
                    }
                }
            })

        collectLatest(viewModel.searchList) { animeData ->
            pagingController.submitData(animeData)
        }
    }

    companion object {
        private const val ANIMATION_DURATION = 300L
    }
}
