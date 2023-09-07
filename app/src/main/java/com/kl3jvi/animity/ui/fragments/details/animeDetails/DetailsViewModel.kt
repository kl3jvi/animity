package com.kl3jvi.animity.ui.fragments.details.animeDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.downloader.DownloadState
import com.kl3jvi.animity.data.downloader.Downloader
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.domain.repositories.DetailsRepository
import com.kl3jvi.animity.domain.repositories.FavoriteRepository
import com.kl3jvi.animity.domain.repositories.PlayerRepository
import com.kl3jvi.animity.domain.repositories.UserRepository
import com.kl3jvi.animity.type.MediaListStatus
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.asResult
import com.kl3jvi.animity.utils.ifChanged
import com.kl3jvi.animity.utils.logError
import com.kl3jvi.animity.utils.or1
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val detailsRepository: DetailsRepository,
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository,
    private val playerRepository: PlayerRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val downloader: Downloader,
) : ViewModel() {

    val passedAniListMedia = MutableStateFlow(AniListMedia())
    val reverseState = MutableStateFlow(false)
    val isScheduled = passedAniListMedia.flatMapLatest {
        userRepository.getScheduleStatusLocally(it.idAniList)
    }.map { it != null }
    val downloadState = MutableStateFlow(DownloadState.UNKNOWN)

    val episodeList: StateFlow<EpisodeListUiState> = passedAniListMedia.flatMapLatest { media ->
        favoriteRepository.getGogoUrlFromAniListId(media.idAniList)
            .asResult()
            .flatMapLatest { result ->
                when (result) {
                    is Result.Error -> flowOf(EpisodeListUiState.Error)
                    Result.Loading -> flowOf(EpisodeListUiState.Loading)
                    is Result.Success -> {
                        detailsRepository.fetchEpisodeList(
                            episodeUrl = result.data,
                            malId = media.idMal.or1(),
                            extra = listOf(media),
                        ).map { episodes ->
                            val episodesChunked = episodes.chunked(50)
                            val episodeChunksTitles = episodes.chunked(50) {
                                val firstEpisodeNumber = it.first().getEpisodeNumberOnly()
                                val lastEpisodeNumber = it.last().getEpisodeNumberOnly()
                                "Episodes ${firstEpisodeNumber ?: "-"} - ${lastEpisodeNumber ?: "-"}"
                            }

                            EpisodeListUiState.Success(
                                episodeChunks = episodesChunked,
                                chunkTitles = episodeChunksTitles,
                            )
                        }
                    }
                }
            }
    }.flowOn(ioDispatcher)
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5_000),
            EpisodeListUiState.Loading,
        )

    /**
     * > The function updates the anime as favorite in the AniList website
     */
    fun updateAnimeFavorite() {
        viewModelScope.launch(ioDispatcher) {
            passedAniListMedia.flatMapLatest {
                userRepository.markAnimeAsFavorite(it.idAniList)
                    .ifChanged()
                    .catch { error -> logError(error) }
            }.collect()
        }
    }

    fun downloadEpisode(
        episodeUrl: EpisodeModel,
    ) {
        viewModelScope.launch(
            ioDispatcher + CoroutineExceptionHandler { _, throwable ->
                Log.e("Error", throwable.toString())
            },
        ) {
            playerRepository.getMediaUrl(
                url = episodeUrl.episodeUrl,
                extra = listOf("naruto"),
            )
                .asResult()
                .ifChanged()
                .collect { result ->
                    when (result) {
                        is Result.Error -> {
                            Log.e(
                                "Error",
                                result.exception.toString(),
                                result.exception,
                            )
                            downloadState.value = DownloadState.FAILED
                        }

                        Result.Loading -> downloadState.value = DownloadState.QUEUED

                        is Result.Success -> {
                            val videoUrl = result.data.first().first()
                            downloader.downloadM3U8Content(videoUrl)
                            downloadState.value = DownloadState.COMPLETED
                        }
                    }
                }
        }
    }

    fun changeAnimeStatus(status: MediaListStatus) {
        viewModelScope.launch(ioDispatcher) {
            passedAniListMedia.flatMapLatest {
                detailsRepository.changeAnimeStatus(it.idAniList, status)
                    .ifChanged()
                    .catch { error -> logError(error) }
            }.collect()
        }
    }

    fun updateAnimeScheduleStatus(aniListMedia: AniListMedia) {
        viewModelScope.launch(ioDispatcher) {
            userRepository.markAnimeForSchedule(aniListMedia)
        }
    }
}

sealed interface EpisodeListUiState {
    object Loading : EpisodeListUiState
    object Error : EpisodeListUiState
    data class Success(
        val episodeChunks: List<List<EpisodeModel>>,
        val chunkTitles: List<String>,
    ) : EpisodeListUiState
}
