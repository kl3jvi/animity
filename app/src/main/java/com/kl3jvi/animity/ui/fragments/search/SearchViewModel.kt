package com.kl3jvi.animity.ui.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.domain.repositories.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRepository: SearchRepository,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _searchList = MutableStateFlow(PagingData.empty<AniListMedia>())
    val searchList = _searchList.asStateFlow()

    private var textQuery = ""
    private var searchJob: Job? = null

    fun onSearchQueryChanged(query: String) {
        val newQuery = query.trim().takeIf { it.length >= 2 } ?: ""
        if (textQuery != newQuery) {
            textQuery = newQuery
            executeSearch()
        }
    }

    private fun executeSearch() {
        // Cancel any in-flight searches
        searchJob?.cancel()
        searchJob = viewModelScope.launch(ioDispatcher) {
            searchRepository.fetchAniListSearchData(textQuery)
                .cachedIn(viewModelScope)
                .debounce(500)
                .collect { _searchList.value = it }
        }
    }

    override fun onCleared() {
        super.onCleared()
        searchJob?.cancel()
    }
}
