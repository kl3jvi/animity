package com.kl3jvi.animity.ui.fragments.details

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.google.android.material.chip.Chip
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentDetailsBinding
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.activities.player.PlayerActivity
import com.kl3jvi.animity.ui.adapters.CustomEpisodeAdapter
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.*
import com.kl3jvi.animity.utils.Constants.Companion.getBackgroundColor
import com.kl3jvi.animity.utils.Constants.Companion.getColor
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class DetailsFragment : BaseFragment<DetailsViewModel, FragmentDetailsBinding>() {


    override val viewModel: DetailsViewModel by viewModels()
    private val args: DetailsFragmentArgs by navArgs()
    private val animeDetails get() = args.animeDetails
    private val episodeAdapter by lazy { CustomEpisodeAdapter(this, animeDetails.title) }
    private lateinit var menu: Menu
    private lateinit var title: String
    private var check = false
    private var anilistId: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun observeViewModel() {
        fetchAnimeInfo()
        fetchEpisodeList()
        showLatestEpisodeReleaseTime()
        getAnilistId()
        animeDetails.let { animeInfo ->
            binding.apply {
                detailsPoster.load(animeInfo.imageUrl) { crossfade(true) }
                episodeListRecycler.layoutManager = LinearLayoutManager(requireContext())
                resultTitle.text = animeInfo.title
                title = animeInfo.title
                episodeListRecycler.adapter = episodeAdapter
            }
            animeInfo.categoryUrl?.let { url ->
                viewModel.passUrl(url)
            }
//            viewModel.passCategoryUrl(animeInfo.categoryUrl)
        }
    }

    override fun initViews() {

    }

    private fun getAnilistId() {
//        collectFlow(viewModel.getAnilistId(anime = args.animeDetails)) { idData ->
//            anilistId = idData.data?.media?.id
//        }
    }


    private fun fetchAnimeInfo() {
        observeLiveData(viewModel.animeInfo, viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Success -> {
                    res.data?.let { info ->
                        binding.animeInfoLayout.textOverview.text = info.plotSummary
                        binding.releaseDate.text = info.releasedTime
                        binding.status.text = info.status
                        binding.type.text = info.type

                        binding.animeInfoLayout.textOverview.visibility = VISIBLE
                        binding.releaseDate.visibility = VISIBLE
                        binding.status.visibility = VISIBLE
                        binding.type.visibility = VISIBLE
                        binding.detailsProgress.visibility = VISIBLE

                        // Check if the type is movie and this makes invisible the listview of the episodes
                        if (info.type == " Movie") {
                            binding.apply {
                                resultEpisodesText.visibility = GONE
                                binding.episodeListRecycler.visibility = GONE
                                resultPlayMovie.visibility = VISIBLE
                                imageButton.visibility = GONE
                            }
                        } else {
                            binding.apply {
                                resultEpisodesText.visibility = VISIBLE
                                resultPlayMovie.visibility = GONE
                                episodeListRecycler.visibility = VISIBLE
                            }
                        }
                        info.genre.forEach { data ->
                            binding.genreGroup.removeAllViews()
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
                    }
                }
                is Resource.Loading -> {
                    binding.animeInfoLayout.textOverview.visibility = GONE
                    binding.releaseDate.visibility = GONE
                    binding.status.visibility = GONE
                    binding.type.visibility = GONE
                }
                is Resource.Error -> {
//                    showSnack(binding.root, res.message)
                }
                null -> {}
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.favorite_menu, menu)
        this.menu = menu
        observeDatabase()
        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun observeDatabase() {
        viewModel.isOnDatabase.observe(viewLifecycleOwner) {
            if (it) Log.e("Exists", "on Database")
            else Log.e("Does Not", "on Database")
            check = it
            if (!check) {
                menu[1].setIcon(R.drawable.ic_favorite_uncomplete)
            } else {
                menu[1].setIcon(R.drawable.ic_favorite_complete)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_to_favorites -> {
                check = if (!check) {
                    menu[1].setIcon(R.drawable.ic_favorite_complete)
                    if (isGuestLogin()) {
//                        viewModel.insert(anime = args.animeDetails)
                    } else {
                        collectFlow(viewModel.updateAnimeFavorite(anilistId)) {}
                    }

//                    collectFlow(viewModel.getAnilistId(anime = args.animeDetails)) { idData ->
//                        val id = idData.data?.media?.id
//                        collectFlow(viewModel.updateAnimeFavorite(id)) {}
//                    }
//                    showSnack(binding.root, "Anime added to Favorites")
                    true
                } else {
                    menu[1].setIcon(R.drawable.ic_favorite_uncomplete)
                    if (isGuestLogin()) {
//                        viewModel.delete(anime = args.animeDetails)
                    } else {
                        collectFlow(viewModel.updateAnimeFavorite(anilistId)) {}
                    }
//                    viewModel.delete(anime = args.animeDetails)
//                    collectFlow(viewModel.getAnilistId(anime = args.animeDetails)) { idData ->
//                        val id = idData.data?.media?.id
//                        collectFlow(viewModel.updateAnimeFavorite(id)) {}
//                    }
//                    showSnack(binding.root, "Anime removed from Favorites")
                    false
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    @ExperimentalCoroutinesApi
    private fun fetchEpisodeList() {
        viewModel.episodeList.observe(viewLifecycleOwner) { episodeListResponse ->
            episodeListResponse.data?.let { episodeList ->
                episodeAdapter.submitList(episodeList.reversed())
                binding.detailsProgress.visibility = GONE
                binding.resultEpisodesText.text = "${episodeList.size} Episodes"
                var check = false
                binding.imageButton.setOnClickListener {
                    check = if (!check) {
                        binding.imageButton.load(R.drawable.ic_up_arrow) {
                            crossfade(true)
                        }
                        episodeAdapter.submitList(episodeList)
                        true
                    } else {
                        binding.imageButton.load(R.drawable.ic_down_arrow) {
                            crossfade(true)
                        }
                        episodeAdapter.submitList(episodeList.reversed())
                        false
                    }
                }
                if (episodeList.isNotEmpty()) {
                    binding.resultPlayMovie.setOnClickListener {
                        requireActivity().launchActivity<PlayerActivity> {
                            putExtra(
                                Constants.EPISODE_DETAILS,
                                episodeList.first()
                            )
                            putExtra(Constants.ANIME_TITLE, title)
                        }
                        binding.resultPlayMovie.visibility = VISIBLE
                    }
                } else {
                    binding.resultPlayMovie.visibility = GONE
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (requireActivity() is MainActivity) {
            (activity as MainActivity?)?.hideBottomNavBar()
        }
    }

    private fun showLatestEpisodeReleaseTime() {
        viewModel.lastEpisodeReleaseTime.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Success -> {
                    res.data?.let {
                        if (it.time.isNotEmpty()) {
                            binding.nextEpisodeContainer.visibility = VISIBLE
                            binding.releaseTime.text = " ${it.time}"
                        } else binding.nextEpisodeContainer.visibility = GONE
                    }
                }
                is Resource.Error -> {
                    binding.nextEpisodeContainer.visibility = GONE
                }
                is Resource.Loading -> {
                    binding.nextEpisodeContainer.visibility = GONE
                }
            }
        }
    }

    override fun getViewBinding(): FragmentDetailsBinding =
        FragmentDetailsBinding.inflate(layoutInflater)
}

