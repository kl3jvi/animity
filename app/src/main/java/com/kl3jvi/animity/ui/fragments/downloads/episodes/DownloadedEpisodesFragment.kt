package com.kl3jvi.animity.ui.fragments.downloads.episodes

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentDownloadedEpisodesBinding
import com.kl3jvi.animity.episodeList
import com.kl3jvi.animity.ui.activities.player.PlayerActivity
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.collect
import com.kl3jvi.animity.utils.launchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class DownloadedEpisodesFragment : Fragment(R.layout.fragment_downloaded_episodes) {

    companion object {
        fun newInstance() = DownloadedEpisodesFragment()
    }

    private val viewModel: DownloadedEpisodesViewModel by viewModels()

    private val args: DownloadedEpisodesFragmentArgs by navArgs()
    private var binding: FragmentDownloadedEpisodesBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDownloadedEpisodesBinding.bind(view)
        initViews()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    private fun initViews() {
        viewModel.setAnimeId(args.animePassId)
        collect(viewModel.downloadedEpisodes) { downloadList ->
            binding?.epoxyRecyclerView?.withModels {
                downloadList.sortedBy { it.episodeNumber }.forEach { animeDetails ->
                    episodeList {
                        id(animeDetails.episodeUrl)
                        episodeInfo(animeDetails)
                        clickListener { view ->
                            // navigate to player
                            requireContext().launchActivity<PlayerActivity> {
                                putExtra(Constants.EPISODE_URL, animeDetails.episodeUrl)
                                putExtra(Constants.ANILIST_ID, animeDetails.animeId)
                                putExtra(Constants.ANIME_TITLE, animeDetails.episodeNumber)
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }
}
