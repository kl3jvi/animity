package com.kl3jvi.animity.ui.fragments.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.databinding.FragmentFavoritesBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.adapters.CustomFavoriteAdapter
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FavoritesFragment : BaseFragment<FavoritesViewModel, FragmentFavoritesBinding>() {

    private lateinit var favoriteAdapter: CustomFavoriteAdapter
    override val viewModel: FavoritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun isGuestLogin(): Boolean {
        return (activity as MainActivity).isGuestLogin
    }

    override fun observeViewModel() {
        if (isGuestLogin())
            observeDatabase()
        else observeAnilist()
    }

    override fun initViews() {
        binding.favoritesRecycler.apply {
            layoutManager = GridLayoutManager(requireActivity(), 3)
            favoriteAdapter = CustomFavoriteAdapter()
            setHasFixedSize(true)
            adapter = favoriteAdapter
        }

        binding.swipeLayout.setOnRefreshListener {
            if (!isGuestLogin())
                observeAnilist()
            else
                showLoading(false)
        }
    }

    private fun observeDatabase() {
        viewModel.favoriteFromDatabase.observe(viewLifecycleOwner) { animeList ->
            if (animeList.isNotEmpty()) {
                favoriteAdapter.submitList(animeList)
                binding.favoritesRecycler.visibility = View.VISIBLE
            } else {
                binding.favoritesRecycler.visibility = View.GONE
                binding.nothingSaved.visibility = View.VISIBLE
            }
        }
    }


    private fun observeAnilist() {
        collectFlow(viewModel.favoriteAnimesList) { animeList ->
            val list = animeList.data?.user?.favourites?.anime?.edges?.map {
                AnimeMetaModel(
                    title = it?.node?.title?.userPreferred.toString(),
                    imageUrl = it?.node?.coverImage?.large.toString(),
                    categoryUrl = "category/${
                        it?.node?.title?.romaji
                            .toString()
                            .replace(" ", "-")
                            .replace(":", "")
                            .replace(";", "")
                            .replace(".", "").replace("//", "").replace("/", "")
                            .lowercase(Locale.getDefault())
                    }"
                )
            }
            if (!list.isNullOrEmpty()) {
                favoriteAdapter.submitList(list)
                if (!viewModel.isDataSynced()) // Save favorites do local db for not making more requests
                    viewModel.insertRemoteToLocalDb(list)
                binding.favoritesRecycler.visibility = View.VISIBLE
                showLoading(false)
            } else {
                binding.favoritesRecycler.visibility = View.GONE
                binding.nothingSaved.visibility = View.VISIBLE
            }
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

    override fun getViewBinding(): FragmentFavoritesBinding =
        FragmentFavoritesBinding.inflate(layoutInflater)
}
