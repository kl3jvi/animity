package com.kl3jvi.animity.view.fragments.details


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.kl3jvi.animity.databinding.FragmentDetailsBinding
import com.kl3jvi.animity.utils.Constants.Companion.getBackgroundColor
import com.kl3jvi.animity.utils.Constants.Companion.getColor
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.view.activities.MainActivity
import com.kl3jvi.animity.view.adapters.CustomEpisodeAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()
    private val viewModel: DetailsViewModel by viewModels()
    private lateinit var episodeAdapter: CustomEpisodeAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        fetchAnimeInfo()
        fetchEpisodeList()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        args.animeDetails.let { animeInfo ->
            binding.apply {
                detailsPoster.load(animeInfo.imageUrl) {
                    crossfade(true)
                    diskCachePolicy(CachePolicy.ENABLED)
                }
                episodeListRecycler.layoutManager =
                    androidx.recyclerview.widget.LinearLayoutManager(requireContext())
                resultTitle.text = animeInfo.title
                episodeAdapter =
                    com.kl3jvi.animity.view.adapters.CustomEpisodeAdapter(requireParentFragment())
                binding.episodeListRecycler.adapter = episodeAdapter
            }
            animeInfo.categoryUrl?.let { url ->
                viewModel.passUrl(url)
            }
        }


    }

    private fun fetchAnimeInfo() {
        viewModel.animeInfo.observe(viewLifecycleOwner, { res ->
            when (res) {
                is Resource.Success -> {
                    res.data?.let { info ->
                        binding.expandTextView.text = info.plotSummary
                        binding.releaseDate.text = info.releasedTime
                        binding.status.text = info.status
                        binding.type.text = info.type

                        binding.expandTextView.visibility = View.VISIBLE
                        binding.releaseDate.visibility = View.VISIBLE
                        binding.status.visibility = View.VISIBLE
                        binding.type.visibility = View.VISIBLE


                        // Check if the type is movie and this makes invisible the listview of the episodes
                        if (info.type == " Movie") {
                            binding.apply {
                                resultEpisodesText.visibility = View.GONE
                                binding.episodeListRecycler.visibility = View.GONE
                                resultPlayMovie.visibility = View.VISIBLE
                                imageButton.visibility = View.GONE
                            }
                        } else{
                            binding.apply {
                                resultEpisodesText.visibility = View.VISIBLE
                                resultPlayMovie.visibility = View.GONE

                                episodeListRecycler.visibility = View.VISIBLE
                            }
                        }

                        info.genre.forEach { data ->
                            val chip = Chip(requireContext())
                            chip.apply {
                                text = data.genreName
                                setTextColor(Color.WHITE)
                                chipStrokeColor = getColor()
                                chipStrokeWidth = 3f
                                chipBackgroundColor = getBackgroundColor()
                            }
                            binding.genreGroup.addView(chip)
                        }
                        viewModel.passEpisodeData(info.id, info.endEpisode, info.alias)
                    }
                }
                is Resource.Loading -> {
                    binding.expandTextView.visibility = View.GONE
                    binding.releaseDate.visibility = View.GONE
                    binding.status.visibility = View.GONE
                    binding.type.visibility = View.GONE
                }
                is Resource.Error -> {
                    showSnack(res.message)
                }
            }
        })
    }

    private fun fetchEpisodeList() {
        viewModel.episodeList.observe(viewLifecycleOwner, { episodeListResponse ->
            episodeListResponse.data?.let { episodeList ->
                episodeAdapter.getEpisodeInfo(episodeList)
                binding.resultEpisodesText.text = "${episodeList.size} Episodes"
            }
        })
    }


    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavBar()
        }
    }

    private fun showSnack(message: String?) {
        val snack =
            Snackbar.make(binding.root, message ?: "Error Occurred", Snackbar.LENGTH_LONG)
        if (!snack.isShown) {
            snack.show()
        }
    }
}