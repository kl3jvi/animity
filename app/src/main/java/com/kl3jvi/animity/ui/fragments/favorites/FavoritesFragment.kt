package com.kl3jvi.animity.ui.fragments.favorites

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentFavoritesBinding
import com.kl3jvi.animity.favoriteAnime
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.base.viewBinding
import com.kl3jvi.animity.utils.NetworkUtils.isConnectedToInternet
import com.kl3jvi.animity.utils.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val viewModel: FavoritesViewModel by viewModels()
    private val binding: FragmentFavoritesBinding by viewBinding()
    private var shouldRefreshFavorites: Boolean = false
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initViews()
    }

    private fun observeViewModel() {
        observeAniList()
    }

    private fun initViews() {

        viewModel.shouldRefresh.value = shouldRefreshFavorites
        binding.swipeLayout.setOnRefreshListener {
            viewModel.shouldRefresh.value = shouldRefreshFavorites
            shouldRefreshFavorites = !shouldRefreshFavorites
            observeAniList()

        }
    }


    private fun observeAniList() {
        collectFlow(viewModel.favoriteAniListAnimeList) { animeList ->


            binding.favoritesRecycler.layoutManager = GridLayoutManager(requireContext(), 3)
            binding.favoritesRecycler.withModels {
                animeList?.forEach { media ->
                    favoriteAnime {
                        id(media.idAniList)
                        clickListener { _ ->
                            val directions =
                                FavoritesFragmentDirections.actionNavigationFavoritesToNavigationDetails(
                                    media
                                )
                            findNavController().navigate(directions)
                        }
                        animeInfo(media)
                    }
                }
            }
            binding.favoritesRecycler.isVisible = !animeList.isNullOrEmpty()
            binding.nothingSaved.isVisible = animeList.isNullOrEmpty()
            showLoading(false)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.swipeLayout.isRefreshing = isLoading
    }


    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavBar()
        }
    }


    override fun onStart() {
        super.onStart()
        handleNetworkChanges()
    }

    private fun handleNetworkChanges() {
        requireActivity().isConnectedToInternet(viewLifecycleOwner) { isConnected ->
            binding.noInternetResult.noInternet.isVisible = !isConnected
            binding.favoritesRecycler.isVisible = isConnected
        }
    }
}
