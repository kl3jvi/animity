package com.kl3jvi.animity.ui.fragments.details

import android.util.Log
import androidx.lifecycle.*
import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.MediaIdFromNameQuery
import com.kl3jvi.animity.ToggleFavouriteMutation
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.domain.use_cases.*
import com.kl3jvi.animity.persistence.AnimeRepository
import com.kl3jvi.animity.utils.Constants.Companion.SAVED_STATE_KEY
import com.kl3jvi.animity.utils.NetworkResource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
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
    private val stateHandle: SavedStateHandle
) : ViewModel() {

    private val _url = MutableLiveData<String>()
    private val _animeId = MutableLiveData<Int>()

    private val animeCategoryUrl = stateHandle.getLiveData<AnimeMetaModel>(SAVED_STATE_KEY)

    init {

        Log.e("anime info passed", animeCategoryUrl.value.toString())
    }

    val animeInfo = Transformations.switchMap(animeCategoryUrl) { animeMetaModel ->
        if (animeMetaModel.categoryUrl.isNullOrEmpty()) {
            getGogoUrlFromFavoritesId(animeMetaModel.id)
                .flatMapLatest {
                    when (it) {
                        is NetworkResource.Failed -> emptyFlow()
                        is NetworkResource.Success -> {
                            getAnimeDetailsUseCase.fetchAnimeInfo(
                                it.data.pages.data.entries.first().value.url.replace("vc","gg")
                            )
                        }
                    }
                }.asLiveData()
        } else {
            getAnimeDetailsUseCase.fetchAnimeInfo(animeMetaModel.categoryUrl.toString())
                .asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)
        }
    }

//    val animeId = Transformations.switchMap(_url) { string ->
//        getAnimeDetailsUseCase.fetchAnimeInfo(string).flatMapLatest {
//            getAnimeDetailsFromAnilistUseCase(1,it.data.)
//        }.asLiveData()
//    }


    @ExperimentalCoroutinesApi
    val episodeList = Transformations.switchMap(_url) { list ->
        getAnimeDetailsUseCase.fetchAnimeInfo(list).flatMapLatest { info ->
            getAnimeDetailsUseCase.fetchEpisodeList(
                info.data?.id,
                info.data?.endEpisode,
                info.data?.alias
            )
        }.asLiveData(Dispatchers.Default + viewModelScope.coroutineContext)
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
