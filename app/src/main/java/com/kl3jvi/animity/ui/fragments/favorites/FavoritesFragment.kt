package com.kl3jvi.animity.ui.fragments.favorites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.kl3jvi.animity.databinding.FragmentFavoritesBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.adapters.CustomFavoriteAdapter
import com.kl3jvi.animity.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

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

    override fun observeViewModel() {
        observeDatabase()
    }

    override fun initViews() {
        binding.favoritesRecycler.apply {
            layoutManager = GridLayoutManager(requireActivity(), 3)
            favoriteAdapter = CustomFavoriteAdapter()
            setHasFixedSize(true)
            adapter = favoriteAdapter
        }
    }

    private fun observeDatabase() {
        viewModel.favoriteAnimesList.observe(viewLifecycleOwner, { animeList ->
            if (animeList.isNotEmpty()) {
                favoriteAdapter.submitList(animeList)
                binding.favoritesRecycler.visibility = View.VISIBLE
            } else {
                binding.favoritesRecycler.visibility = View.GONE
                binding.nothingSaved.visibility = View.VISIBLE
            }
        })
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
