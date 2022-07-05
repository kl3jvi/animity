package com.kl3jvi.animity.ui.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.domain.repositories.fragment_repositories.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,

    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {


    private val _searchList = MutableStateFlow<PagingData<AniListMedia>>(PagingData.empty())
    val searchList = _searchList.asStateFlow()

    val queryString = MutableStateFlow("")

    init {
        searchData()
    }

    private fun searchData() {
        viewModelScope.launch(ioDispatcher) {
            queryString.collectLatest { query ->
                searchRepository.fetchAniListSearchData(query)
                    .cachedIn(viewModelScope)
                    .distinctUntilChanged()
                    .collect { _searchList.value = it }
            }
        }
    }
}