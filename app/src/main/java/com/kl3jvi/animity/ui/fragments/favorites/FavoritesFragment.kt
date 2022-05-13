package com.kl3jvi.animity.ui.fragments.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.kl3jvi.animity.databinding.FragmentFavoritesBinding
import com.kl3jvi.animity.favoriteAnime
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.NetworkUtils.isConnectedToInternet
import com.kl3jvi.animity.utils.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FavoritesViewModel, FragmentFavoritesBinding>() {

    override val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private var shouldRefreshFavorites: Boolean = false

    override fun observeViewModel() {}

    override fun initViews() {
        viewModel.shouldRefresh.value = shouldRefreshFavorites
        binding.swipeLayout.setOnRefreshListener {
            viewModel.shouldRefresh.value = shouldRefreshFavorites
            shouldRefreshFavorites = !shouldRefreshFavorites
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
            if (isConnected) observeAniList()
            binding.noInternetResult.noInternet.isVisible = !isConnected
            binding.favoritesRecycler.isVisible = isConnected
        }
    }

    override fun getViewBinding(): FragmentFavoritesBinding =
        FragmentFavoritesBinding.inflate(layoutInflater)

}
