package com.kl3jvi.animity.ui.fragments.favorites

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.databinding.FragmentFavoritesBinding
import com.kl3jvi.animity.data.model.AnimeMetaModel
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.adapters.CustomFavoriteAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteAdapter: CustomFavoriteAdapter
    private val viewModel: FavoritesViewModel by viewModels()
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        firebaseAnalytics = Firebase.analytics
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavoritesBinding.inflate(inflater, container, false)
        initViews()
        observeDatabase()
        return binding.root
    }

    private fun initViews() {
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

    fun navigateToDetails(animeDetails: AnimeMetaModel) {
        try {
            findNavController().navigate(
                FavoritesFragmentDirections.actionNavigationFavoritesToNavigationDetails(
                    animeDetails
                )
            )
            if (requireActivity() is MainActivity) {
                (activity as MainActivity?)?.hideBottomNavBar()
            }
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.showBottomNavBar()
        }
    }
}
