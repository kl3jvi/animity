package com.kl3jvi.animity.ui.fragments.search

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kl3jvi.animity.data.model.AnimeMetaModel
import com.kl3jvi.animity.domain.use_cases.GetSearchResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchResultUseCase: GetSearchResultUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var currentQueryValue: String? = null
    private var currentSearchResult: Flow<PagingData<AnimeMetaModel>>? = null

    fun searchAnimes(queryString: String): Flow<PagingData<AnimeMetaModel>> {
        currentQueryValue = queryString
        val newResult: Flow<PagingData<AnimeMetaModel>> =
            getSearchResultUseCase(queryString).cachedIn(viewModelScope)
        currentSearchResult = newResult
        return newResult
    }
}