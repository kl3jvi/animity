package com.kl3jvi.animity.ui.fragments.details.animeDetails

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.fragment.app.Fragment
import com.kl3jvi.animity.R
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.databinding.FragmentEpisodeContainerBinding
import com.kl3jvi.animity.episodeLarge
import com.kl3jvi.animity.ui.activities.player.PlayerActivity
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.launchActivity

class EpisodeContainer : Fragment(R.layout.fragment_episode_container) {

    private lateinit var binding: FragmentEpisodeContainerBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEpisodeContainerBinding.bind(view)
        val episodes = requireArguments().getParcelableArrayList<EpisodeModel>(ARG_EPISODE_LIST)!!
        val animeData = requireArguments().getParcelable<AniListMedia>(ANIME_DATA)
        val desiredPosition = requireArguments().getInt(DESIRED_POSITION)
        bindEpisodeList(episodes, animeData, desiredPosition) {
            goToDesiredPosition()
        }
    }

    private fun goToDesiredPosition() {

    }

    private fun bindEpisodeList(
        episodes: List<EpisodeModel>,
        animeDetails: AniListMedia?,
        desiredPosition: Int,
        listBuildCallBack: () -> Unit
    ) {
        val animation = AnimationUtils.loadAnimation(requireContext(), R.anim.blink_animation)
        binding.episodeListRecycler.withModels {
            episodes.forEachIndexed { index, episodeModel ->
                episodeLarge {
                    id(index)
                    clickListener { _ ->
                        requireContext().launchActivity<PlayerActivity> {
                            putExtra(Constants.EPISODE_DETAILS, episodeModel)
                            putExtra(
                                Constants.ANIME_TITLE,
                                animeDetails?.title?.userPreferred
                            )
                            putExtra(Constants.MAL_ID, animeDetails?.idMal)
                        }
                    }
                    showTitle(episodeModel.episodeName.isNotEmpty())
                    isFiller(episodeModel.isFiller)
                    imageUrl(
                        when {
                            animeDetails?.streamingEpisode?.getOrNull(index)?.thumbnail == null -> {
                                animeDetails?.bannerImage?.ifEmpty { animeDetails.coverImage.large }
                            }

                            animeDetails.streamingEpisode.getOrNull(index)?.thumbnail != null -> {
                                animeDetails.streamingEpisode.getOrNull(index)?.thumbnail
                            }

                            else -> animeDetails.coverImage.large
                        }
                    )
                    episodeInfo(episodeModel)
                    onBind { _, view, _ ->
                        if (index == desiredPosition) {
                            // Apply a translation animation to the root view of the data binding layout
                            view.dataBinding.root.startAnimation(animation)
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

        fun newInstance(
            episodeList: List<EpisodeModel>,
            animeDetails: AniListMedia,
            desiredPosition: Int
        ): EpisodeContainer {
            return EpisodeContainer().apply {
                arguments = Bundle().apply {
                    putParcelableArrayList(ARG_EPISODE_LIST, ArrayList(episodeList))
                    putParcelable(ANIME_DATA, animeDetails)
                    putInt(DESIRED_POSITION, desiredPosition)
                }
            }
        }
    }
}
