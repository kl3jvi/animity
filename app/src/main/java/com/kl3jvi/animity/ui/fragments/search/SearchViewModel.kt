package com.kl3jvi.animity.ui.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.domain.repositories.SearchRepository
import com.kl3jvi.animity.type.MediaSort
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _searchList = MutableStateFlow<PagingData<AniListMedia>>(PagingData.empty())
    val searchList: StateFlow<PagingData<AniListMedia>> = _searchList.asStateFlow()
    private val sortTypes = mutableListOf<SortType>()
    private var lastSearchQuery = ""

    private val searchQueryChannel = Channel<SearchQuery>(Channel.CONFLATED)
    private val searchFlow = searchQueryChannel
        .receiveAsFlow()
        .debounce(500)
        .filter { it.query.length >= 2 }
        .flatMapLatest { searchRepository.fetchAniListSearchData(it.query, sortTypes) }
        .cachedIn(viewModelScope)
        .flowOn(ioDispatcher)

    init {
        viewModelScope.launch {
            searchFlow.collect {
                _searchList.value = it
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        lastSearchQuery = query.trim()
        searchQueryChannel.trySend(SearchQuery(lastSearchQuery, sortTypes)).isSuccess
    }

    fun toggleSortType(sortType: SortType) {
        if (sortTypes.contains(sortType)) {
            sortTypes.remove(sortType)
        } else {
            sortTypes.add(sortType)
        }
        onSearchQueryChanged(lastSearchQuery)
    }
}

data class SearchQuery(
    val query: String,
    val sortTypes: MutableList<SortType>
)

enum class SortType {
    TITLE,
    START_DATE,
    POPULARITY,
    AVERAGE_SCORE,
    TRENDING,
    FAVOURITES,
    EPISODES;

    fun toMediaSort(): MediaSort {
        return when (this) {
            TITLE -> MediaSort.TITLE_ENGLISH
            START_DATE -> MediaSort.START_DATE
            POPULARITY -> MediaSort.POPULARITY
            AVERAGE_SCORE -> MediaSort.SCORE
            TRENDING -> MediaSort.TRENDING
            FAVOURITES -> MediaSort.FAVOURITES
            EPISODES -> MediaSort.EPISODES
        }
    }
}
