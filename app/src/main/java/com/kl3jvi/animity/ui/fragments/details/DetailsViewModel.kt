package com.kl3jvi.animity.ui.fragments.details

import androidx.lifecycle.*
import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.domain.use_cases.GetAnimeDetailsFromAnilistUseCase
import com.kl3jvi.animity.domain.use_cases.GetAnimeDetailsUseCase
import com.kl3jvi.animity.domain.use_cases.GetGogoUrlFromFavoritesId
import com.kl3jvi.animity.domain.use_cases.MarkAnimeAsFavoriteUseCase
import com.kl3jvi.animity.persistence.AnimeRepository
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.logError
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val animeRepository: AnimeRepository,
    private val markAnimeAsFavoriteUseCase: MarkAnimeAsFavoriteUseCase,
    private val getAnimeDetailsFromAnilistUseCase: GetAnimeDetailsFromAnilistUseCase,
    private val getGogoUrlFromFavoritesId: GetGogoUrlFromFavoritesId,
) : ViewModel() {

    private val _url = MutableLiveData<String>()
    val animeMetaModel = MutableStateFlow<AnimeMetaModel?>(null)

    private val _anilistId = MutableStateFlow<Int>(-1)
    private val anilistId = _anilistId.asStateFlow()

    init {
        getAnimeInfo()
        getAniListId()
    }


    private val _animeInfo = MutableStateFlow<Resource<AnimeInfoModel>?>(null)
    val animeInfo = _animeInfo.asStateFlow()


    private val _episodeList = MutableStateFlow<Resource<List<EpisodeModel>>?>(null)
    val episodeList = _episodeList.asStateFlow()


    private fun getAnimeInfo() {

        viewModelScope.launch(Dispatchers.IO) {
            animeMetaModel.collect { animeDetails ->
                animeDetails?.let { animeMetaModel ->
                    if (animeDetails.categoryUrl.isNullOrEmpty()) {

                        getGogoUrlFromFavoritesId(animeMetaModel.id).flatMapLatest { result ->
                            when (result) {
                                is NetworkResource.Failed -> emptyFlow()
                                is NetworkResource.Success -> {
                                    async { fetchEpisodeList(result.data.pages?.data?.entries?.first()?.value?.url.orEmpty()) }.await()
                                        .collect { _episodeList.value = it }
                                    async {
                                        getAnimeDetailsUseCase.fetchAnimeInfo(
                                            result.data.pages?.data?.entries?.first()?.value?.url.orEmpty()
                                        )
                                    }.await()
                                }
                            }
                        }.collectLatest { _animeInfo.value = it }
                    }
//                    else {
//                        async { fetchEpisodeList(animeMetaModel.categoryUrl.orEmpty()) }.await()
//                            .collect { _episodeList.value = it }
//                        async {
//                            getAnimeDetailsUseCase.fetchAnimeInfo(animeMetaModel.categoryUrl.toString())
//                        }.await()
//                            .collectLatest { _animeInfo.value = it }
//                    }
                }
            }
        }
    }

    private fun fetchEpisodeList(url: String): Flow<Resource<List<EpisodeModel>>> {
        return getAnimeDetailsUseCase.fetchAnimeInfo(url).flatMapLatest { info ->
            getAnimeDetailsUseCase.fetchEpisodeList(
                info.data?.id,
                info.data?.endEpisode,
                info.data?.alias
            )
        }.catch { e -> logError(e) }

    }


    val lastEpisodeReleaseTime = Transformations.switchMap(_url) {
        getAnimeDetailsUseCase.fetchEpisodeReleaseTime(it.split("/").last())
            .asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)
    }

    val isOnDatabase = Transformations.switchMap(_url) { url ->
        getAnimeDetailsUseCase.checkIfExists(url)
            .asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)
    }

    fun passUrl(url: String) {
        _url.value = url
    }

    fun insert(anime: AnimeMetaModel) = viewModelScope.launch(Dispatchers.IO) {
        animeRepository.insertFavoriteAnime(anime)
    }

    fun updateAnimeFavorite() {
        viewModelScope.async {
            anilistId.collect {
                if (it != -1)
                    markAnimeAsFavoriteUseCase(it).catch { error -> logError(error) }.collect {}
            }
        }

    }


    fun getAniListId() {
        viewModelScope.launch(Dispatchers.IO) {
            animeMetaModel.collect { it ->
                getAnimeDetailsFromAnilistUseCase(it?.title.toString())
                    .catch { e -> e.printStackTrace() }
                    .collect {
                        _anilistId.value = it.data?.media?.id ?: -1
                    }
            }
        }
    }

    fun delete(anime: AnimeMetaModel) = viewModelScope.launch(Dispatchers.IO) {
        animeRepository.deleteAnime(anime)
    }

//    fun getUrlFromId(id: String) {
//        getGogoUrlFromFavoritesId(id)
//    }

}
