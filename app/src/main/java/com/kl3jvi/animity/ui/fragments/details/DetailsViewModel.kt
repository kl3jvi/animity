package com.kl3jvi.animity.ui.fragments.details

import android.util.Log
import androidx.lifecycle.*
import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.MediaIdFromNameQuery
import com.kl3jvi.animity.ToggleFavouriteMutation
import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.domain.use_cases.*
import com.kl3jvi.animity.persistence.AnimeRepository
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val animeRepository: AnimeRepository,
    private val getEpisodeInfoUseCase: GetEpisodeInfoUseCase,
    private val markAnimeAsFavoriteUseCase: MarkAnimeAsFavoriteUseCase,
    private val getAnimeDetailsFromAnilistUseCase: GetAnimeDetailsFromAnilistUseCase,
    private val getGogoUrlFromFavoritesId: GetGogoUrlFromFavoritesId,
) : ViewModel() {

    private val _url = MutableLiveData<String>()

    val animeMetaModel = MutableStateFlow<AnimeMetaModel?>(null)

    init {
        getAnimeInfo()
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
                                    getAnimeDetailsUseCase.fetchAnimeInfo(
                                        result.data.pages.data.entries.first().value.url.replace(
                                            "vc",
                                            "gg"
                                        )
                                    ).flatMapLatest { info ->
                                        getAnimeDetailsUseCase.fetchEpisodeList(
                                            info.data?.id,
                                            info.data?.endEpisode,
                                            info.data?.alias
                                        )
                                    }.collectLatest { _episodeList.value = it }

                                    getAnimeDetailsUseCase.fetchAnimeInfo(
                                        result.data.pages.data.entries.first().value.url.replace(
                                            "vc",
                                            "gg"
                                        )
                                    )
                                }
                            }
                        }.collectLatest { _animeInfo.value = it }
                    } else {
                        getAnimeDetailsUseCase.fetchAnimeInfo(animeMetaModel.categoryUrl.toString())
                            .flatMapLatest { info ->
                                getAnimeDetailsUseCase.fetchEpisodeList(
                                    info.data?.id,
                                    info.data?.endEpisode,
                                    info.data?.alias
                                )
                            }.collectLatest { _episodeList.value = it }

                        getAnimeDetailsUseCase.fetchAnimeInfo(animeMetaModel.categoryUrl.toString())
                            .collectLatest { _animeInfo.value = it }

                    }
                }
            }
        }
    }


//    val animeId = Transformations.switchMap(_url) { string ->
//        getAnimeDetailsUseCase.fetchAnimeInfo(string).flatMapLatest {
//            getAnimeDetailsFromAnilistUseCase(1,it.data.)
//        }.asLiveData()
//    }


//    @ExperimentalCoroutinesApi
//    val episodeList = Transformations.switchMap(animeCategoryUrl) { animeMetaModel ->
//        if (animeMetaModel.categoryUrl.isNullOrEmpty()) {
//
//        }
//        getAnimeDetailsUseCase.fetchAnimeInfo("list").flatMapLatest { info ->
//            getAnimeDetailsUseCase.fetchEpisodeList(
//                info.data?.id,
//                info.data?.endEpisode,
//                info.data?.alias
//            )
//        }.asLiveData(Dispatchers.Default + viewModelScope.coroutineContext)
//
//    }


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
        Log.e("Category URL", url)
    }

    fun insert(anime: AnimeMetaModel) = viewModelScope.launch(Dispatchers.IO) {
        animeRepository.insertFavoriteAnime(anime)
    }

    fun updateAnimeFavorite(id: Int?): Flow<ApolloResponse<ToggleFavouriteMutation.Data>> {
        return markAnimeAsFavoriteUseCase(id)
    }

    fun getAnilistId(anime: AnimeMetaModel?): Flow<ApolloResponse<MediaIdFromNameQuery.Data>> {
        return getAnimeDetailsFromAnilistUseCase(anime?.title.toString())
    }

    fun delete(anime: AnimeMetaModel) = viewModelScope.launch(Dispatchers.IO) {
        animeRepository.deleteAnime(anime)
    }

//    fun getUrlFromId(id: String) {
//        getGogoUrlFromFavoritesId(id)
//    }

}
