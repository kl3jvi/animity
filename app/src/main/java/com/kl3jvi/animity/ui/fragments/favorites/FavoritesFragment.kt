package com.kl3jvi.animity.ui.fragments.favorites

import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.kl3jvi.animity.R
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.databinding.FragmentFavoritesBinding
import com.kl3jvi.animity.ui.fragments.StateManager
import com.kl3jvi.animity.utils.BottomNavScrollListener
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.collect
import com.kl3jvi.animity.utils.epoxy.FavoritesSearchController
import com.kl3jvi.animity.utils.epoxy.setupBottomNavScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import javax.inject.Inject

@Suppress("SameParameterValue")
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites), StateManager {
    private val viewModel: FavoritesViewModel by viewModels()
    private var binding: FragmentFavoritesBinding? = null
    private lateinit var pagingController: FavoritesSearchController
    private val listener: BottomNavScrollListener by lazy {
        requireActivity() as BottomNavScrollListener
    }
    private var job: Job? = null

    @Inject
    lateinit var analytics: Analytics

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)
        pagingController = FavoritesSearchController(analytics)
        observeAniList()
        initViews()
    }

    private fun initViews() {
        binding?.swipeLayout?.setOnRefreshListener {
            observeAniList()
            viewModel.refreshFavorites()
        }
        binding?.favoritesRecycler
            ?.setupBottomNavScrollListener(listener)
            ?.apply {
                setController(pagingController)
                val widthOfView = 120f
                val numberOfColumns = calculateNoOfColumns(widthOfView)
                layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
            }
    }

    private fun observeAniList() {
        job?.cancel()
        job =
            collect(viewModel.favoritesList) { favoritesUiState ->
                Log.e("FAVORITES", favoritesUiState.toString())
                when (favoritesUiState) {
                    is FavouriteState.Error -> {
                        showSnack(binding?.root, "Error getting favorites")
                        binding?.nothingSaved?.isVisible = true
                        binding?.swipeLayout?.isRefreshing = false
                    }

                    FavouriteState.Loading -> {
                        binding?.swipeLayout?.isRefreshing = true
                    }

                    is FavouriteState.Success -> {
                        binding?.swipeLayout?.isRefreshing = false
                        binding?.favoritesRecycler?.apply {
                            pagingController.submitData(favoritesUiState.data)
                        }
                    }
                }
            }
    }

    private fun calculateNoOfColumns(columnWidthDp: Float): Int {
        val displayMetrics: DisplayMetrics = requireContext().resources?.displayMetrics!!
        val screenWidthDp = displayMetrics.widthPixels / displayMetrics.density
        return (screenWidthDp / columnWidthDp + 0.5).toInt() // +0.5 for correct rounding to int.
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun showLoading(show: Boolean) = Unit

    override fun handleError(e: Throwable) = showSnack(binding?.root, e.message)
}
