@file:OptIn(ExperimentalCoroutinesApi::class)

package com.kl3jvi.animity.ui.fragments.details.animeDetails

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.downloader.DownloadState
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.databinding.FragmentEpisodeContainerBinding
import com.kl3jvi.animity.episodeLarge
import com.kl3jvi.animity.ui.activities.player.PlayerActivity
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.launchActivity
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
class EpisodeContainer : Fragment(R.layout.fragment_episode_container) {
    private lateinit var binding: FragmentEpisodeContainerBinding
    private val viewModel: DetailsViewModel by activityViewModels()

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEpisodeContainerBinding.bind(view)
        val args = requireArguments()
        val episodes = args.getParcelableArrayList<EpisodeModel>(ARG_EPISODE_LIST)!!
        val animeData = args.getParcelable<AniListMedia>(ANIME_DATA)
        val desiredPosition = args.getInt(DESIRED_POSITION)
        val increase = args.getInt(INCREASER)
        val step = if (increase == 0) 0 else increase * 50
        bindEpisodeList(episodes, animeData, desiredPosition, step, {
            binding.episodeListRecycler.scrollToPosition(desiredPosition)
        }) {
            val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink_animation)
            it.startAnimation(animation)
        }
    }

    private fun bindEpisodeList(
        episodes: List<EpisodeModel>,
        animeDetails: AniListMedia?,
        desiredPosition: Int,
        increaser: Int,
        listBuildCallBack: () -> Unit,
        runBlinkAnimation: (View) -> Unit,
    ) {
        binding.episodeListRecycler.withModels {
            episodes.forEachIndexed { index, episodeModel ->
                episodeLarge {
                    id(episodeModel.episodeUrl)
                    vm(viewModel)
                    animeDetails(animeDetails)
                    clickListener { _ ->
                        requireContext().launchActivity<PlayerActivity> {
                            putExtra(Constants.EPISODE_DETAILS, episodeModel)
                            putExtra(
                                Constants.ANIME_TITLE,
                                animeDetails?.title?.userPreferred,
                            )
                            putExtra(Constants.ANILIST_ID, animeDetails?.idMal)
                        }
                    }
                    showTitle(episodeModel.episodeName.isNotEmpty())
                    isDownloaded(episodeModel.isDownloaded)
                    isFiller(episodeModel.isFiller)
                    downloadStatus(DownloadState.STATE_QUEUED)
                    imageUrl(
                        when {
                            animeDetails?.streamingEpisode?.getOrNull(index + increaser)?.thumbnail == null -> {
                                animeDetails?.bannerImage?.ifEmpty { animeDetails.coverImage.large }
                            }

                            animeDetails.streamingEpisode.getOrNull(index + increaser)?.thumbnail != null -> {
                                animeDetails.streamingEpisode.getOrNull(index + increaser)?.thumbnail
                            }

                            else -> animeDetails.coverImage.large
                        },
                    )
                    episodeInfo(episodeModel)
                    onBind { _, view, _ ->
                        if (index == desiredPosition - 1) {
                            // Apply a translation animation to the root view of the data binding layout
                            runBlinkAnimation(view.dataBinding.root)
                        }
                    }
                }
            }
            listBuildCallBack()
        }
    }

    companion object {
        private const val ARG_EPISODE_LIST = "episode_list"
        private const val ANIME_DATA = "anime_data"
        private const val DESIRED_POSITION = "desired_position"
        private const val INCREASER = "increaser"

        fun newInstance(
            episodeList: List<EpisodeModel>,
            increaser: Int,
            animeDetails: AniListMedia,
            desiredPosition: Int,
        ): EpisodeContainer {
            return EpisodeContainer().apply {
                arguments =
                    Bundle().apply {
                        putParcelableArrayList(ARG_EPISODE_LIST, ArrayList(episodeList))
                        putParcelable(ANIME_DATA, animeDetails)
                        putInt(INCREASER, increaser)
                        putInt(DESIRED_POSITION, desiredPosition)
                    }
            }
        }
    }
}
