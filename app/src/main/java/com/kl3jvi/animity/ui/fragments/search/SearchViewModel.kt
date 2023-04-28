package com.kl3jvi.animity.ui.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.domain.repositories.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
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

    private val searchQueryChannel = Channel<String>(Channel.CONFLATED)
    private val searchFlow = searchQueryChannel
        .receiveAsFlow()
        .debounce(500)
        .filter { it.length >= 2 }
        .distinctUntilChanged()
        .flatMapLatest { searchRepository.fetchAniListSearchData(it) }
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
        searchQueryChannel.trySend(query.trim()).isSuccess
    }
}
