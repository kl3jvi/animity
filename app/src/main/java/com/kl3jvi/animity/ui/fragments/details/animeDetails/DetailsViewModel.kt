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

    val animeMetaModel = MutableStateFlow<AniListMedia?>(null)


    val episodeList: StateFlow<EpisodeListUiState> = animeMetaModel.flatMapLatest { media ->
        favoriteRepository.getGogoUrlFromAniListId(media?.idAniList.or1()).asResult().map { result ->
            when (result) {
                is Result.Error -> EpisodeListUiState.Error
                Result.Loading -> EpisodeListUiState.Loading
                is Result.Success -> {

                    val episodeListFlow = detailsRepository.fetchAnimeInfo(
                        episodeUrl = result.data.pages?.data?.entries?.first()?.value?.url.orEmpty()
                    ).flatMapLatest { info ->
                        detailsRepository.fetchEpisodeList(
                            id = info.id,
                            endEpisode = info.endEpisode,
                            alias = info.alias,
                            malId = media?.idMal.or1()
                        )
                    }.catch { e ->
                        logError(e)
                    }.mapNotNull { list ->
                        list.ifEmpty { emptyList() }
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
     * > The function updates the anime as favorite in the anilist website
     */
    fun updateAnimeFavorite() {
        viewModelScope.launch(ioDispatcher) {
            animeMetaModel.collect {
                userRepository.markAnimeAsFavorite(it?.idAniList)
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
