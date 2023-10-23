package com.kl3jvi.animity.ui.fragments.search

import android.animation.ValueAnimator
import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.ViewTreeObserver
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.kl3jvi.animity.R
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.data.enums.SearchMode
import com.kl3jvi.animity.data.enums.SortType
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.User
import com.kl3jvi.animity.databinding.FragmentSearchBinding
import com.kl3jvi.animity.utils.BottomNavScrollListener
import com.kl3jvi.animity.utils.collect
import com.kl3jvi.animity.utils.collectLatest
import com.kl3jvi.animity.utils.dismissKeyboard
import com.kl3jvi.animity.utils.epoxy.PagingSearchController
import com.kl3jvi.animity.utils.epoxy.setupBottomNavScrollListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.fragment_search) {
    private val viewModel: SearchViewModel by viewModels()
    private var binding: FragmentSearchBinding? = null
    private lateinit var listener: BottomNavScrollListener

    private val animeController: PagingSearchController<AniListMedia> by lazy {
        PagingSearchController(
            analytics,
            SearchMode.ANIME,
            viewModel.myId.toInt(),
        )
    }
    private val userController: PagingSearchController<User> by lazy {
        PagingSearchController(
            analytics,
            SearchMode.USERS,
            viewModel.myId.toInt(),
        )
    }

    @Inject
    lateinit var analytics: Analytics

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSearchBinding.bind(view)

        initViews()
        observeViewModel()
        setupSortChips()
        listener = requireActivity() as BottomNavScrollListener
    }

    /**
     * It sets up the search view and recycler view.
     */
    private fun initViews() {
        binding?.apply {
            mainSearch.setOnQueryTextListener(
                object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String): Boolean {
                        dismissKeyboard(binding?.mainSearch)
                        return false
                    }

                    override fun onQueryTextChange(query: String): Boolean {
                        viewModel.onSearchQueryChanged(query)
                        return false
                    }
                },
            )

            toggleGroup.check(R.id.btn_anime)
            toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) {
                    when (checkedId) {
                        R.id.btn_anime -> {
                            viewModel.switchToAnimeSearch()
                            setupSortChips()
                        }

                        R.id.btn_users -> {
                            viewModel.switchToUserSearch()
                        }
                    }
                }
            }
        }
    }

    private fun setupSortChips() {
        binding?.apply {
            val chipMap =
                mapOf(
                    sortTitle to SortType.TITLE,
                    sortDate to SortType.START_DATE,
                    sortPopularity to SortType.POPULARITY,
                    sortAverageScore to SortType.AVERAGE_SCORE,
                    sortTrending to SortType.TRENDING,
                    sortFavorites to SortType.FAVOURITES,
                    sortEpisodes to SortType.EPISODES,
                )

            val previousChipStates =
                mutableMapOf<Chip, Triple<ColorStateList?, ColorStateList?, Float>>()

            for ((chip, sortType) in chipMap) {
                chip.setOnClickListener {
                    val isSelected = !chip.isSelected
                    chip.isSelected = isSelected

                    if (isSelected) {
                        viewModel.toggleSortType(sortType)
                        previousChipStates[chip] =
                            Triple(
                                chip.chipStrokeColor,
                                chip.chipBackgroundColor,
                                chip.chipStrokeWidth,
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
            View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
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

        collect(viewModel.currentSearchMode) {
            when (it) {
                SearchMode.ANIME -> {
                    binding?.searchRecycler
                        ?.setupBottomNavScrollListener(listener)
                        ?.setController(animeController)
                    binding?.searchRecycler?.layoutManager = LinearLayoutManager(requireContext())
                    binding?.sortView?.viewTreeObserver?.addOnGlobalLayoutListener(
                        object :
                            ViewTreeObserver.OnGlobalLayoutListener {
                            override fun onGlobalLayout() {
                                binding?.sortView?.viewTreeObserver?.removeOnGlobalLayoutListener(this)
                                animeController.addInterceptor {
                                    if (it.size == 0) {
                                        collapseView(sortView)
                                    } else {
                                        expandView(sortView)
                                    }
                                }
                            }
                        },
                    )
                }

                SearchMode.USERS -> {
                    collapseView(sortView)
                    binding?.searchRecycler?.setController(userController)
                    val widthOfView = 110f
                    val numberOfColumns = calculateNoOfColumns(widthOfView)
                    binding?.searchRecycler?.layoutManager =
                        GridLayoutManager(requireContext(), numberOfColumns)
                }
            }
        }

        collectLatest(viewModel.searchList) { animeData ->
            if (viewModel.currentSearchMode.value == SearchMode.ANIME) {
                animeController.submitData(animeData)
            }
        }

        // Handle User search results
        collectLatest(viewModel.usersList) { usersData ->
            if (viewModel.currentSearchMode.value == SearchMode.USERS) {
                userController.submitData(usersData)
            }
        }
    }

    private fun calculateNoOfColumns(columnWidthDp: Float): Int {
        val displayMetrics: DisplayMetrics = requireContext().resources?.displayMetrics!!
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt() // +0.5 for correct rounding to int.
    }

    companion object {
        private const val ANIMATION_DURATION = 300L
    }
}
