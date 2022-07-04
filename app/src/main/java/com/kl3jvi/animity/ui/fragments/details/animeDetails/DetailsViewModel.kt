package com.kl3jvi.animity.ui.fragments.details.animeDetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.domain.use_cases.GetAnimeDetailsUseCase
import com.kl3jvi.animity.domain.use_cases.GetGogoUrlFromAniListId
import com.kl3jvi.animity.domain.use_cases.MarkAnimeAsFavoriteUseCase
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.asResult
import com.kl3jvi.animity.utils.logError
import com.kl3jvi.animity.utils.logMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val markAnimeAsFavoriteUseCase: MarkAnimeAsFavoriteUseCase,
    private val getGogoUrlFromAniListId: GetGogoUrlFromAniListId,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {


    val animeMetaModel = MutableStateFlow<AniListMedia?>(null)

    init {
        getAnimeInfo()
    }

    private val _animeInfo = MutableStateFlow<Result<AnimeInfoModel>?>(null)
    val animeInfo = _animeInfo.asStateFlow()


    private val _episodeList = MutableStateFlow<List<EpisodeModel>?>(null)
    val episodeList = _episodeList.asStateFlow()

    /**
     * `getAnimeInfo()` is a function that fetches the anime info from the gogoanime website and
     * updates the `_animeInfo` LiveData object
     */
    private fun getAnimeInfo() {
        viewModelScope.launch(ioDispatcher) {
            animeMetaModel.collect { animeDetails ->
                animeDetails?.let { animeMetaModel ->
                    getGogoUrlFromAniListId(animeMetaModel.idAniList).asResult()
                        .flatMapLatest { result ->
                            when (result) {
                                is Result.Error -> emptyFlow()
                                Result.Loading -> emptyFlow()
                                is Result.Success -> {
                                    val (_, second) = awaitAll(
                                        async {
                                            fetchEpisodeList(
                                                result.data.pages?.data?.entries?.first()?.value?.url.orEmpty(),
                                                animeMetaModel.idAniList
                                            )
                                        },
                                        async {
                                            getAnimeDetailsUseCase.fetchAnimeInfo(
                                                result.data.pages?.data?.entries?.first()?.value?.url.orEmpty()
                                            ).asResult()
                                        }
                                    )
                                    second as? Flow<Result<AnimeInfoModel>?> ?: emptyFlow()
                                }
                            }
                        }.collectLatest { _animeInfo.value = it }
                }
            }
        }
    }

    /**
     * `fetchAnimeInfo` returns a `Single<Resource<AnimeInfo>>` which is then flatMapped to
     * `fetchEpisodeList` which returns a `Single<Resource<EpisodeList>>`
     *
     * @param url The url of the anime you want to fetch the episode list from.
     * @return A list of episodes
     */
    private suspend fun fetchEpisodeList(url: String, malId: Int) {

        return getAnimeDetailsUseCase.fetchAnimeInfo(url).flatMapLatest { info ->
            info.let {
                getAnimeDetailsUseCase.fetchEpisodeList(
                    it.id,
                    it.endEpisode,
                    it.alias,
                    malId
                )
            }
        }.catch { e -> logError(e) }.asResult().collect {
            when (it) {
                is Result.Error -> logMessage(it.exception?.message)
                Result.Loading -> {}
                is Result.Success -> _episodeList.value = it.data
            }
        }
    }

    /**
     * > When the user clicks the favorite button, we want to update the database to reflect the new
     * favorite status
     */
    fun updateAnimeFavorite() {
        viewModelScope.launch(ioDispatcher) {
            animeMetaModel.collect {
                markAnimeAsFavoriteUseCase(it?.idAniList)
                    .catch { error -> logError(error) }
                    .collect {}
            }
        }
    }
}
