package com.kl3jvi.animity.view.fragments.details

import androidx.lifecycle.*
import com.kl3jvi.animity.domain.GetAnimeDetailsUseCase
import com.kl3jvi.animity.model.entities.AnimeInfoModel
import com.kl3jvi.animity.model.entities.EpisodeModel
import com.kl3jvi.animity.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject


@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getAnimeDetailsUseCase: GetAnimeDetailsUseCase
) : ViewModel() {

    private var _animeInfo = MutableLiveData<Resource<AnimeInfoModel>>()
    private var _episodeList = MutableLiveData<Resource<List<EpisodeModel>>>()

    private val _url = MutableLiveData<String>()
    private val _id = MutableLiveData<String>()
    private val _endEpisode = MutableLiveData<String>()
    private val _alias = MutableLiveData<String>()
    private val episodeData = MediatorLiveData<String>()


//    var animeInfo: LiveData<Resource<AnimeInfoModel>> = _animeInfo
//    var episodeList: LiveData<Resource<List<EpisodeModel>>> = _episodeList

    val animeInfo = Transformations.switchMap(_url) { string ->
        getAnimeDetailsUseCase.fetchAnimeInfo(string).asLiveData()
    }

//
//    val episodeList = Transformations.switchMap(episodeData) {data->
//        getAnimeDetailsUseCase.fetchEpisodeList(data.,data[1],data[2]).asLiveData()
//    }


//    private fun fetchAnimeInfo(url: String) {
//        getAnimeDetailsUseCase.fetchAnimeInfo(url).onEach {
//            _animeInfo.value = it
//        }.launchIn(viewModelScope)
//    }
//
//    private fun fetchEpisodeList(
//        id: String,
//        endEpisode: String,
//        alias: String
//    ) {
//        getAnimeDetailsUseCase.fetchEpisodeList(id, endEpisode, alias).onEach {
//            _episodeList.value = it
//        }.launchIn(viewModelScope)
//    }

    fun passUrl(url: String) {
        _url.value = url
    }

    fun passEpisodeData(
        id: String,
        endEpisode: String,
        alias: String
    ) {
        _id.value = id
        _endEpisode.value = endEpisode
        _alias.value = alias

        episodeData.addSource(_id) { value ->
            episodeData.setValue(value)
        }

        episodeData.addSource(_endEpisode) { value ->
            episodeData.setValue(value)
        }
        episodeData.addSource(_alias) { value ->
            episodeData.setValue(value)
        }

    }


//    fun fetchAnimeInfo(url: String) = liveData(Dispatchers.IO) {
//        emit(Resource.loading(data = null))
//        try {
//            emit(
//                Resource.success(
//                    data = HtmlParser.parseAnimeInfo(
//                        detailsRepository.fetchAnimeInfo(
//                            Constants.getHeader(),
//                            url
//                        ).string()
//                    )
//                )
//            )
//            data.postValue(
//                HtmlParser.parseAnimeInfo(
//                    detailsRepository.fetchAnimeInfo(
//                        Constants.getHeader(),
//                        url
//                    ).string()
//                )
//            )
//        } catch (exception: Exception) {
//            emit(Resource.error(data = null, message = exception.message ?: "error Occurred!"))
//        }
//    }
//
//
//    fun fetchEpisodeList(
//        id: String,
//        endEpisode: String,
//        alias: String
//    ) = liveData(Dispatchers.IO) {
//        emit(Resource.loading(data = null))
//        try {
//            emit(
//                Resource.success(
//                    data = HtmlParser.fetchEpisodeList(
//                        detailsRepository.fetchEpisodeList(
//                            header = Constants.getHeader(),
//                            id = id,
//                            endEpisode = endEpisode,
//                            alias = alias
//                        ).string()
//                    )
//                )
//            )
//        } catch (exception: Exception) {
//            emit(Resource.error(data = null, message = exception.message ?: "error Occurred!"))
//        }
//    }


}

