package com.kl3jvi.animity.ui.fragments.favorites

import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.kl3jvi.animity.R
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.databinding.FragmentFavoritesBinding
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.UiResult
import com.kl3jvi.animity.utils.collect
import com.kl3jvi.animity.utils.epoxy.FavoritesSearchController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import javax.inject.Inject

@Suppress("SameParameterValue")
@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val viewModel: FavoritesViewModel by viewModels()
    private var binding: FragmentFavoritesBinding? = null
    private var favoritesJob: Job? = null
    private var shouldRefreshFavorites: Boolean = false
    private lateinit var pagingController: FavoritesSearchController

    @Inject
    lateinit var analytics: Analytics

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentFavoritesBinding.bind(view)
        pagingController = FavoritesSearchController(analytics)
        initViews()
        observeAniList()
    }

    private fun initViews() {
        binding?.swipeLayout?.setOnRefreshListener {
            viewModel.refreshFavorites()
        }
    }

    private fun observeAniList() {
        binding?.favoritesRecycler?.apply {
            setController(pagingController)
            val widthOfView = 120f
            val numberOfColumns = calculateNoOfColumns(widthOfView)
            layoutManager = GridLayoutManager(requireContext(), numberOfColumns)
        }

        favoritesJob = collect(viewModel.favoritesList) { favoritesUiState ->
            when (favoritesUiState) {
                is UiResult.Error -> {
                    showSnack(binding?.root, "Error getting favorites")
                    binding?.nothingSaved?.isVisible = true
                    binding?.swipeLayout?.isRefreshing = false
                }

                UiResult.Loading -> {
                    binding?.swipeLayout?.isRefreshing = true
                }

                is UiResult.Success -> {
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
        // letting go of the resources to avoid memory leak.
        binding = null
        favoritesJob?.cancel()
    }
}
