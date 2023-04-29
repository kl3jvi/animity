package com.kl3jvi.animity.ui.fragments.details.animeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.domain.repositories.DetailsRepository
import com.kl3jvi.animity.domain.repositories.FavoriteRepository
import com.kl3jvi.animity.domain.repositories.UserRepository
import com.kl3jvi.animity.type.MediaListStatus
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.asResult
import com.kl3jvi.animity.utils.ifChanged
import com.kl3jvi.animity.utils.logError
import com.kl3jvi.animity.utils.or1
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.distinctUntilChanged
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
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    val animeMetaModel = MutableStateFlow(AniListMedia())
    val reverseState = MutableStateFlow(false)

    val episodeList: StateFlow<EpisodeListUiState> =
        animeMetaModel.distinctUntilChanged { old, _ ->
            old != AniListMedia()
        }.flatMapLatest { media ->
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
                                extra = listOf(media.idMal)
                            ).map { episodes ->
                                // Split the list of episodes into chunks of 50 or less
                                val episodeChunks = episodes.chunked(50) {
                                    val firstEpisodeNumber =
                                        it.first().getEpisodeNumberOnly().toInt()
                                    val lastEpisodeNumber =
                                        it.last().getEpisodeNumberOnly().toInt()
                                    "Episodes $firstEpisodeNumber - $lastEpisodeNumber"
                                }
                                EpisodeListUiState.Success(episodes.chunked(50), episodeChunks)
                            }
                        }
                    }
                }
        }.flowOn(Dispatchers.Default)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(5_000),
                EpisodeListUiState.Loading
            )

    /**
     * > The function updates the anime as favorite in the AniList website
     */
    fun updateAnimeFavorite() {
        viewModelScope.launch(ioDispatcher) {
            animeMetaModel.flatMapLatest {
                userRepository.markAnimeAsFavorite(it.idAniList).ifChanged()
                    .catch { error -> logError(error) }
            }.collect()
        }
    }

    fun changeAnimeStatus(status: MediaListStatus) {
        viewModelScope.launch(ioDispatcher) {
            animeMetaModel.flatMapLatest {
                detailsRepository.changeAnimeStatus(it.idAniList, status)
                    .ifChanged()
                    .catch { error -> logError(error) }
            }.collect()
        }
    }
}

sealed interface EpisodeListUiState {
    object Loading : EpisodeListUiState
    object Error : EpisodeListUiState
    data class Success(
        val episodeChunks: List<List<EpisodeModel>>,
        val chunkTitles: List<String>
    ) : EpisodeListUiState
}
