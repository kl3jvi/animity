package com.kl3jvi.animity.view.fragments.details


import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import com.google.android.material.chip.Chip
import com.google.android.material.snackbar.Snackbar
import com.kl3jvi.animity.databinding.FragmentDetailsBinding
import com.kl3jvi.animity.model.network.ApiHelper
import com.kl3jvi.animity.model.network.RetrofitBuilder
import com.kl3jvi.animity.utils.Constants.Companion.getColor
import com.kl3jvi.animity.utils.Constants.Companion.getBackgroundColor
import com.kl3jvi.animity.utils.Status
import com.kl3jvi.animity.view.adapters.CustomEpisodeAdapter
import com.kl3jvi.animity.view.factory.ViewModelFactory


class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val viewModel: DetailsViewModel by viewModels {
        ViewModelFactory(ApiHelper(RetrofitBuilder.apiService),null)
    }
    private lateinit var episodeAdapter: CustomEpisodeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val args: DetailsFragmentArgs by navArgs()

        args.animeDetails.let { animeInfo ->
            binding.apply {
                detailsPoster.load(animeInfo.imageUrl) {
                    crossfade(true)
                    diskCachePolicy(CachePolicy.ENABLED)
                }
                episodeListRecycler.layoutManager = LinearLayoutManager(requireContext())
                resultTitle.text = animeInfo.title
                episodeAdapter = CustomEpisodeAdapter(requireParentFragment())
                binding.episodeListRecycler.adapter = episodeAdapter
            }
            animeInfo.categoryUrl?.let { url ->
                fetchAnimeInfo(url)
            }
        }
    }


    private fun fetchEpisodeList(id: String, endEpisode: String, alias: String) {
        viewModel.fetchEpisodeList(id, endEpisode, alias)
            .observe(viewLifecycleOwner) { episodeListResponse ->
                episodeListResponse.data?.let { episodeList ->
                    episodeAdapter.getEpisodeInfo(episodeList)
                    if (episodeList.size == 1) {
                        binding.apply {
                            resultEpisodesText.visibility = View.GONE
                            binding.episodeListRecycler.visibility = View.GONE
                            resultPlayMovie.visibility = View.VISIBLE
                        }
                    } else {
                        binding.apply {
                            resultEpisodesText.visibility = View.VISIBLE
                            resultPlayMovie.visibility = View.GONE
                            resultEpisodesText.text = "${episodeList.size} Episodes"
                            episodeListRecycler.visibility = View.VISIBLE
                            imageButton.setOnClickListener {

                            }
                        }
                    }
                }
            }
    }


    private fun fetchAnimeInfo(url: String) {
        viewModel.fetchAnimeInfo(url).observe(viewLifecycleOwner) { res ->
            res?.let { resource ->
                when (resource.status) {
                    Status.SUCCESS -> {
                        resource.data?.let { info ->
                            binding.expandTextView.text = info.plotSummary
                            binding.releaseDate.text = info.releasedTime
                            binding.status.text = info.status
                            binding.type.text = info.type

                            binding.expandTextView.visibility = View.VISIBLE
                            binding.releaseDate.visibility = View.VISIBLE
                            binding.status.visibility = View.VISIBLE
                            binding.type.visibility = View.VISIBLE

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
                            fetchEpisodeList(info.id, info.endEpisode, info.alias)
                        }
                    }
                    Status.ERROR -> {
                        showSnack(res.message)
                    }
                    Status.LOADING -> {

                        binding.expandTextView.visibility = View.GONE
                        binding.releaseDate.visibility = View.GONE
                        binding.status.visibility = View.GONE
                        binding.type.visibility = View.GONE

                    }
                }
            }
        }
    }




    private fun showSnack(message: String?) {
        val snack = Snackbar.make(binding.root, message ?: "Error Occurred", Snackbar.LENGTH_LONG)
        if (!snack.isShown) {
            snack.show()
        }
    }
}