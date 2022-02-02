package com.kl3jvi.animity.ui.fragments.details

import android.util.Log
import androidx.lifecycle.*
import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.MediaIdFromNameQuery
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.domain.use_cases.GetAnimeDetailsFromAnilistUseCase
import com.kl3jvi.animity.domain.use_cases.GetAnimeDetailsUseCase
import com.kl3jvi.animity.domain.use_cases.GetEpisodeInfoUseCase
import com.kl3jvi.animity.domain.use_cases.MarkAnimeAsFavoriteUseCase
import com.kl3jvi.animity.persistence.AnimeRepository
import com.kl3jvi.animity.persistence.EpisodeDao
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val animeRepository: AnimeRepository,
    private val getEpisodeInfoUseCase: GetEpisodeInfoUseCase,
    private val markAnimeAsFavoriteUseCase: MarkAnimeAsFavoriteUseCase,
    private val getAnimeDetailsFromAnilistUseCase: GetAnimeDetailsFromAnilistUseCase,
    private val episodeDao: EpisodeDao
) : ViewModel() {

    private val _url = MutableLiveData<String>()
    private val _animeId = MutableLiveData<Int>()

    val animeInfo = Transformations.switchMap(_url) { string ->
        getAnimeDetailsUseCase.fetchAnimeInfo(string).asLiveData()
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
            ).onEach {
                val response = it.data
                response?.map { episodeModel ->
                    if (episodeDao.isEpisodeOnDatabase(episodeModel.episodeUrl)) {
                        episodeModel.percentage =
                            episodeDao.getEpisodeContent(episodeModel.episodeUrl)
                                .getWatchedPercentage()
                    }
                }
            }
        }.asLiveData(Dispatchers.Default + viewModelScope.coroutineContext)
    }


    val lastEpisodeReleaseTime = Transformations.switchMap(_url) {
        getAnimeDetailsUseCase.fetchEpisodeReleaseTime(it.split("/").last()).asLiveData()
    }

    val isOnDatabase = Transformations.switchMap(_animeId) { id ->
        getAnimeDetailsUseCase.checkIfExists(id).asLiveData()
    }

    fun passUrl(url: String) {
        _url.value = url
        Log.e("Category URL", url)
    }

    fun passId(id: Int) {
        _animeId.value = id
    }


    fun insert(anime: AnimeMetaModel, id: Int?) = viewModelScope.launch {
        animeRepository.insertFavoriteAnime(anime)
        markAnimeAsFavoriteUseCase(id)
    }

    fun getAnilistId(anime: AnimeMetaModel?): Flow<ApolloResponse<MediaIdFromNameQuery.Data>> {
        return getAnimeDetailsFromAnilistUseCase(anime?.title.toString())
    }

    fun delete(anime: AnimeMetaModel, id: Int?) = viewModelScope.launch {
        animeRepository.deleteAnime(anime)
        markAnimeAsFavoriteUseCase(id)
    }

}
