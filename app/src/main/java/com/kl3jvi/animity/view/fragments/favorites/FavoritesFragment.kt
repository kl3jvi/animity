package com.kl3jvi.animity.view.fragments.favorites

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import com.irozon.alertview.AlertActionStyle
import com.irozon.alertview.AlertStyle
import com.irozon.alertview.AlertView
import com.irozon.alertview.objects.AlertAction
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentFavoritesBinding
import com.kl3jvi.animity.model.entities.AnimeMetaModel
import com.kl3jvi.animity.view.activities.MainActivity
import com.kl3jvi.animity.view.adapters.CustomFavoriteAdapter
import com.kl3jvi.animity.viewmodels.FavoritesViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class FavoritesFragment : Fragment() {
    private var _binding: FragmentFavoritesBinding? = null
    private val binding get() = _binding!!
    private lateinit var favoriteAdapter: CustomFavoriteAdapter
    private val viewModel: FavoritesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
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
            favoriteAdapter = CustomFavoriteAdapter(this@FavoritesFragment, arrayListOf())
            setHasFixedSize(true)
            adapter = favoriteAdapter
        }
    }

    private fun observeDatabase() {
        viewModel.favoriteAnimesList.observe(viewLifecycleOwner, { animeList ->
            if (animeList.isNotEmpty()) {
                favoriteAdapter.addAnimes(animeList)
                binding.favoritesRecycler.visibility = View.VISIBLE
            } else {
                binding.favoritesRecycler.visibility = View.GONE
                binding.nothingSaved.visibility = View.VISIBLE
            }
        })
    }

    fun navigateToDetails(animeDetails: AnimeMetaModel) {
        findNavController().navigate(
            FavoritesFragmentDirections.actionNavigationFavoritesToNavigationDetails(
                animeDetails
            )
        )
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavBar()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.filter_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    @SuppressLint("CheckResult")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {


        when (item.itemId) {
            R.id.filter_animes -> {

                val alert = AlertView("Title", "Message", AlertStyle.DIALOG)
                alert.addAction(AlertAction("Action 1", AlertActionStyle.DEFAULT) { action ->
                    Toast.makeText(requireContext(), action.title, Toast.LENGTH_SHORT).show()
                })
                alert.addAction(AlertAction("Action 2", AlertActionStyle.NEGATIVE) { action ->
                    Toast.makeText(requireContext(), action.title, Toast.LENGTH_SHORT).show()
                })

                alert.show(activity as AppCompatActivity)


            }
            else -> {

            }
        }
        return super.onOptionsItemSelected(item)
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