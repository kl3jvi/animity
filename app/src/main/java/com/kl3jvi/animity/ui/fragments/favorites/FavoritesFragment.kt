package com.kl3jvi.animity.ui.fragments.favorites

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.databinding.FragmentFavoritesBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.adapters.CustomFavoriteAdapter
import com.kl3jvi.animity.ui.base.viewBinding
import com.kl3jvi.animity.utils.collectFlow
import com.kl3jvi.animity.utils.hide
import com.kl3jvi.animity.utils.isGuestLogin
import com.kl3jvi.animity.utils.show
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import java.util.*

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class FavoritesFragment : Fragment(R.layout.fragment_favorites) {

    private val viewModel: FavoritesViewModel by viewModels()
    private val binding: FragmentFavoritesBinding by viewBinding()
    private lateinit var favoriteAdapter: CustomFavoriteAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        favoriteAdapter = CustomFavoriteAdapter()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initViews()
    }

    private fun observeViewModel() {
        if (isGuestLogin()) observeDatabase()
        else observeAniList()
    }

    private fun initViews() {
        binding.favoritesRecycler.apply {
            layoutManager = GridLayoutManager(requireActivity(), 3)
            setHasFixedSize(true)
            adapter = favoriteAdapter
        }

        binding.swipeLayout.setOnRefreshListener {
            if (!isGuestLogin())
                observeAniList()
            else
                showLoading(false)
        }
    }

    private fun observeDatabase() {
        viewModel.favoriteFromDatabase.observe(viewLifecycleOwner) { animeList ->
            if (animeList.isNotEmpty()) {
                favoriteAdapter.submitList(animeList)
                binding.favoritesRecycler.show()
            } else {
                binding.apply {
                    favoritesRecycler.hide()
                    nothingSaved.show()
                }
            }
        }
    }


    private fun observeAniList() {
        collectFlow(viewModel.favoriteAnimesList) { animeList ->
            val list = animeList.data?.user?.favourites?.anime?.edges?.map {
                Log.e("HASHED",
                    it?.node?.title?.romaji?.lowercase(Locale.getDefault()).hashCode().toString()
                )
                AnimeMetaModel(
                    id = it?.node?.title?.romaji?.lowercase(Locale.getDefault()).hashCode(),
                    title = it?.node?.title?.userPreferred.toString(),
                    imageUrl = it?.node?.coverImage?.large.toString(),
                    categoryUrl = "category/${
                        it?.node?.title?.romaji
                            .toString()
                            .replace(" ", "-")
                            .replace(":", "")
                            .replace(";", "")
                            .replace(".", "")
                            .replace("//", "")
                            .replace("/", "")
                            .lowercase(Locale.getDefault())
                    }"
                )
            }
            if (!list.isNullOrEmpty()) {
                favoriteAdapter.submitList(list)
                if (!viewModel.isDataSynced()) { // Save favorites do local db for not making more requests
                    viewModel.insertRemoteToLocalDb(list)
                    viewModel.syncData("remote data synced")
                }
                binding.favoritesRecycler.show()
                showLoading(false)
            } else {
                binding.favoritesRecycler.hide()
                binding.nothingSaved.show()
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
}
