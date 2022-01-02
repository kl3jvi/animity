package com.kl3jvi.animity.ui.fragments.details

import android.app.NotificationManager
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.get
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import coil.request.CachePolicy
import com.google.android.exoplayer2.offline.DownloadRequest
import com.google.android.exoplayer2.offline.DownloadService
import com.google.android.material.chip.Chip
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentDetailsBinding
import com.kl3jvi.animity.services.VideoDownloadService
import com.kl3jvi.animity.ui.activities.main.MainActivity
import com.kl3jvi.animity.ui.activities.player.PlayerActivity
import com.kl3jvi.animity.ui.adapters.CustomEpisodeAdapter
import com.kl3jvi.animity.ui.base.BaseFragment
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Constants.Companion.DOWNLOAD_CHANNEL_ID
import com.kl3jvi.animity.utils.Constants.Companion.getBackgroundColor
import com.kl3jvi.animity.utils.Constants.Companion.getColor
import com.kl3jvi.animity.utils.Constants.Companion.showSnack
import com.kl3jvi.animity.utils.Resource
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject


@AndroidEntryPoint
class DetailsFragment : BaseFragment<DetailsViewModel, FragmentDetailsBinding>() {


    private val args: DetailsFragmentArgs by navArgs()
    private val animeDetails get() = args.animeDetails
    override val viewModel: DetailsViewModel by viewModels()
    private lateinit var episodeAdapter: CustomEpisodeAdapter
    private lateinit var menu: Menu
    private var title: String = ""
    private var check = false
    private lateinit var firebaseAnalytics: FirebaseAnalytics

    @Inject
    lateinit var notificationManager: NotificationManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        firebaseAnalytics = Firebase.analytics
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

        animeDetails.let { animeInfo ->
            binding.apply {
                detailsPoster.load(animeInfo.imageUrl) {
                    crossfade(true)
                    diskCachePolicy(CachePolicy.ENABLED)
                }
                episodeListRecycler.layoutManager = LinearLayoutManager(requireContext())
                episodeAdapter = CustomEpisodeAdapter(this@DetailsFragment, animeInfo.title)
                resultTitle.text = animeInfo.title

                title = animeInfo.title
                binding.episodeListRecycler.adapter = episodeAdapter
            }
            animeInfo.categoryUrl?.let { url ->
                viewModel.passUrl(url)
            }
            viewModel.passId(animeInfo.id)
        }
    }

    override fun initViews() {

    }


    private fun fetchAnimeInfo() {
        viewModel.animeInfo.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Success -> {
                    res.data?.let { info ->
                        binding.expandTextView.text = info.plotSummary
                        binding.releaseDate.text = info.releasedTime
                        binding.status.text = info.status
                        binding.type.text = info.type

                        binding.expandTextView.visibility = VISIBLE
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
                    binding.expandTextView.visibility = GONE
                    binding.releaseDate.visibility = GONE
                    binding.status.visibility = GONE
                    binding.type.visibility = GONE
                }
                is Resource.Error -> {
                    showSnack(binding.root, res.message)
                }
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
            check = it
            if (!check) {
                menu[0].setIcon(R.drawable.ic_favorite_uncomplete)

            } else {
                menu[0].setIcon(R.drawable.ic_favorite_complete)
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.add_to_favorites -> {
                check = if (!check) {
                    menu[0].setIcon(R.drawable.ic_favorite_complete)
                    viewModel.insert(anime = args.animeDetails)
                    showSnack(binding.root, "Anime added to Favorites")
                    true
                } else {
                    menu[0].setIcon(R.drawable.ic_favorite_uncomplete)
                    viewModel.delete(args.animeDetails)
                    showSnack(binding.root, "Anime removed from Favorites")
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
                        val intent = Intent(requireActivity(), PlayerActivity::class.java)
                        intent.putExtra(Constants.EPISODE_DETAILS, episodeList.first())
                        intent.putExtra(Constants.ANIME_TITLE, title)
                        requireContext().startActivity(intent)
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

    @ExperimentalCoroutinesApi
    fun getM3U8EpisodeUrl(episodeUrl: String) {
        viewModel.passDownloadEpisodeUrl(episodeUrl)
        viewModel.downloadEpisodeUrl.observe(viewLifecycleOwner) { res ->
            when (res) {
                is Resource.Success -> {
                    downloadEpisode(res.data.toString())
                }
                is Resource.Error -> {
                    showSnack(binding.root, "Downloading Error")
                }
                is Resource.Loading -> {}
            }
        }
    }

    private fun downloadEpisode(videoM3U8Url: String) {
        val downloadRequest: DownloadRequest =
            DownloadRequest.Builder(
                DOWNLOAD_CHANNEL_ID,
                Uri.parse(videoM3U8Url)
            ).build()

        DownloadService.sendAddDownload(
            requireContext(),
            VideoDownloadService::class.java,
            downloadRequest,
            false
        )
    }

    override fun getViewBinding(): FragmentDetailsBinding =
        FragmentDetailsBinding.inflate(layoutInflater)


}

