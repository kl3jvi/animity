package com.kl3jvi.animity.ui.fragments.details.animeDetails

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.exoplayer2.offline.Download
import com.google.android.exoplayer2.offline.DownloadManager
import com.kl3jvi.animity.data.downloader.Downloader
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.data.model.ui_models.LocalAnime
import com.kl3jvi.animity.data.model.ui_models.LocalEpisode
import com.kl3jvi.animity.domain.repositories.DetailsRepository
import com.kl3jvi.animity.domain.repositories.FavoriteRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.plus
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DetailsViewModel
@Inject
constructor(
    private val detailsRepository: DetailsRepository,
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository,
    private val playerRepository: PlayerRepository,
    private val persistenceRepository: PersistenceRepository,
    private val downloader: Downloader,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    val passedAniListMedia = MutableStateFlow(AniListMedia())
    val reverseState = MutableStateFlow(false)

    val episodeList: StateFlow<EpisodeListUiState> =
        passedAniListMedia.flatMapLatest { media ->
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
                                val episodesChunked = episodes.chunked(20)
                                val episodeChunksTitles =
                                    episodes.chunked(20) {
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
        passedAniListMedia.flatMapLatest { media ->
            userRepository.markAnimeAsFavorite(media.idAniList)
                .ifChanged()
                .catch { error -> logError(error) }
        }.launchIn(viewModelScope + ioDispatcher)
    }

    fun downloadEpisode(episodeModel: EpisodeModel, aniListMedia: AniListMedia) {
        playerRepository.getMediaUrl(url = episodeModel.episodeUrl, extra = listOf("naruto"))
            .asResult()
            .ifChanged()
            .onEach { result ->
                when (result) {
                    is Result.Error -> handleError(result)
                    is Result.Success -> handleSuccess(result, episodeModel, aniListMedia)
                    else -> {}
                }
            }.launchIn(
                viewModelScope + ioDispatcher + CoroutineExceptionHandler { _, throwable ->
                    Log.e("Error", "Download error: ${throwable.message}", throwable)
                }
            )
    }

    private fun handleError(result: Result.Error) {
        Log.e("Error", "Fetching media URL failed: ${result.exception?.message}", result.exception)
    }

    private suspend fun handleSuccess(
        result: Result.Success<List<List<String>>>,
        episodeModel: EpisodeModel,
        aniListMedia: AniListMedia
    ) {
        val videoUrls = result.data
        if (videoUrls.isNotEmpty()) {
            val videoUrl = videoUrls.first().first()
            checkAndInsertAnime(aniListMedia)
            val localEpisode = createLocalEpisode(videoUrl, episodeModel, aniListMedia)
            downloader.downloadVideoUrl(
                videoUrl,
                DownloadListener(localEpisode)
            )
        } else return

    }

    private suspend fun checkAndInsertAnime(aniListMedia: AniListMedia) {
        val animeExistOnDataBase = persistenceRepository.getLocalAnimeById(aniListMedia.idAniList)
        if (animeExistOnDataBase != null) {
            persistenceRepository.incrementDownloadedEpisodesCount(aniListMedia.idAniList)
        } else {
            val localAnime = LocalAnime(
                id = aniListMedia.idAniList,
                title = aniListMedia.title.userPreferred,
                coverImageUrl = aniListMedia.coverImage.large,
                episodesCount = 1
            )
            persistenceRepository.insertLocalAnime(localAnime)
        }
    }

    private fun createLocalEpisode(
        videoUrl: String,
        episodeModel: EpisodeModel,
        aniListMedia: AniListMedia
    ): LocalEpisode {
        return LocalEpisode(
            episodeUrl = videoUrl,
            animeId = aniListMedia.idAniList,
            episodeNumber = episodeModel.getEpisodeNumberAsString(),
            downloaded = true
        )
    }

    inner class DownloadListener(private val localEpisode: LocalEpisode) :
        DownloadManager.Listener {
        override fun onDownloadChanged(
            downloadManager: DownloadManager,
            download: Download,
            finalException: Exception?
        ) {
            super.onDownloadChanged(downloadManager, download, finalException)
            if (download.state == Download.STATE_COMPLETED) {
                viewModelScope.launch(Dispatchers.IO) {
                    persistenceRepository.insertLocalEpisode(localEpisode)
                }
            }
        }
    }

    fun changeAnimeStatus(status: MediaListStatus) {
        passedAniListMedia.flatMapLatest {
            detailsRepository.changeAnimeStatus(it.idAniList, status)
                .ifChanged()
                .catch { error -> logError(error) }
        }.launchIn(viewModelScope + ioDispatcher)
    }
}

sealed interface EpisodeListUiState {
    data object Loading : EpisodeListUiState
    data object Error : EpisodeListUiState
    data class Success(
        val episodeChunks: List<List<EpisodeModel>>,
        val chunkTitles: List<String>,
    ) : EpisodeListUiState
}
