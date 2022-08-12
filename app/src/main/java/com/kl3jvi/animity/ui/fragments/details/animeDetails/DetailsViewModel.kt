package com.kl3jvi.animity.ui.fragments.details.animeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.ToggleFavouriteMutation
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.domain.repositories.DetailsRepository
import com.kl3jvi.animity.domain.repositories.FavoriteRepository
import com.kl3jvi.animity.domain.repositories.UserRepository
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.asResult
import com.kl3jvi.animity.utils.logError
import com.kl3jvi.animity.utils.or1
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val detailsRepository: DetailsRepository,
    private val userRepository: UserRepository,
    private val favoriteRepository: FavoriteRepository,
    private val ioDispatcher: CoroutineDispatcher,
) : ViewModel() {

    val animeMetaModel = MutableStateFlow(AniListMedia())


    val episodeList: StateFlow<EpisodeListUiState> = animeMetaModel.flatMapLatest { media ->
        favoriteRepository.getGogoUrlFromAniListId(media.idAniList).asResult().map { result ->
            when (result) {
                is Result.Error -> EpisodeListUiState.Error
                Result.Loading -> EpisodeListUiState.Loading
                is Result.Success -> {
                    /* Fetching the episode list from the anime url and then combining it with the percentage of the
                        episodes watched. */
                    val episodeListFlow = detailsRepository.fetchAnimeInfo(
                        episodeUrl = result.data.pages?.getGogoUrl().orEmpty()
                    ).flatMapLatest { animeInfo ->
                        detailsRepository.fetchEpisodeList(
                            id = animeInfo.id,
                            endEpisode = animeInfo.endEpisode,
                            alias = animeInfo.alias,
                            malId = media.idMal.or1()
                        ).combine(
                            detailsRepository.getEpisodesPercentage(media.idMal.or1())
                        ) { networkEpisodeList, episodeListFromDataBase ->
                            networkEpisodeList.map { episode ->
                                val contentEpisode =
                                    episodeListFromDataBase.firstOrNull { it.episodeUrl == episode.episodeUrl }
                                if (contentEpisode != null) {
                                    episode.percentage = contentEpisode.getWatchedPercentage()
                                }
                                episode
                            }
                        }
                    }.catch { e ->
                        logError(e)
                    }.mapNotNull { episodeModelList ->
                        episodeModelList.ifEmpty { emptyList() }
                    }
                    EpisodeListUiState.Success(episodeListFlow.firstOrNull() ?: emptyList())
                }
            }
        }
    }.stateIn(
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
                userRepository.markAnimeAsFavorite(it.idAniList)
                    .catch { error -> logError(error) }
                    .flatMapLatest { emptyFlow<ApolloResponse<ToggleFavouriteMutation.Data>>() }
            }
        }
    }
}


sealed interface EpisodeListUiState {
    object Loading : EpisodeListUiState
    object Error : EpisodeListUiState
    data class Success(val data: List<EpisodeModel>) : EpisodeListUiState
}
