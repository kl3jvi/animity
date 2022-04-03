package com.kl3jvi.animity.ui.fragments.details.animeDetails

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import com.kl3jvi.animity.data.model.ui_models.EpisodeModel
import com.kl3jvi.animity.domain.use_cases.GetAnimeDetailsUseCase
import com.kl3jvi.animity.domain.use_cases.GetGogoUrlFromAniListId
import com.kl3jvi.animity.domain.use_cases.MarkAnimeAsFavoriteUseCase
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.utils.logError
import com.kl3jvi.animity.utils.logMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase,
    private val markAnimeAsFavoriteUseCase: MarkAnimeAsFavoriteUseCase,
    private val getGogoUrlFromAniListId: GetGogoUrlFromAniListId,
) : ViewModel() {


    val animeMetaModel = MutableStateFlow<AniListMedia?>(null)

    init {
        getAnimeInfo()
    }


    private val _animeInfo = MutableStateFlow<Resource<AnimeInfoModel>?>(null)
    val animeInfo = _animeInfo.asStateFlow()


    private val _episodeList = MutableStateFlow<List<EpisodeModel>?>(null)
    val episodeList = _episodeList.asStateFlow()


    private fun getAnimeInfo() {
        viewModelScope.launch(Dispatchers.IO) {
            animeMetaModel.collect { animeDetails ->
                animeDetails?.let { animeMetaModel ->
                    getGogoUrlFromAniListId(animeMetaModel.idAniList).flatMapLatest { result ->
                        when (result) {
                            is NetworkResource.Failed -> emptyFlow()
                            is NetworkResource.Success -> {
                                withContext(Dispatchers.Default) {
                                    fetchEpisodeList(result.data.pages?.data?.entries?.first()?.value?.url.orEmpty())
                                    getAnimeDetailsUseCase.fetchAnimeInfo(
                                        result.data.pages?.data?.entries?.first()?.value?.url.orEmpty()
                                    )
                                }
                            }
                        }
                    }.collectLatest { _animeInfo.value = it }
                }
            }
        }
    }

    private suspend fun fetchEpisodeList(url: String) {
        return getAnimeDetailsUseCase.fetchAnimeInfo(url).flatMapLatest { info ->
            getAnimeDetailsUseCase.fetchEpisodeList(
                info.data?.id,
                info.data?.endEpisode,
                info.data?.alias
            )
        }.catch { e -> logError(e) }
            .collect {
                when (it) {
                    is Resource.Error -> {
                        logMessage(it.message)
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        _episodeList.value = it.data
                    }
                }

            }

    }


    //    val lastEpisodeReleaseTime = Transformations.switchMap(_url) {
//        getAnimeDetailsUseCase.fetchEpisodeReleaseTime(it.split("/").last())
//            .asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)
//    }
//
    val isOnDatabase = MutableLiveData<Boolean>()
//    val isOnDatabase = Transformations.switchMap(_url) { url ->
//        getAnimeDetailsUseCase.checkIfExists(url)
//            .asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)
//    }


    fun updateAnimeFavorite() {
        viewModelScope.launch {
            animeMetaModel.collect {
                markAnimeAsFavoriteUseCase(it?.idAniList).catch { error -> logError(error) }
                    .collect {}
            }
        }
    }


}
