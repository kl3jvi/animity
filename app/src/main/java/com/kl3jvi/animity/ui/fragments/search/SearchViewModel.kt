package com.kl3jvi.animity.ui.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.domain.use_cases.GetSearchResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.*
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
            getSearchResultUseCase(queryString).cachedIn(viewModelScope).flowOn(ioDispatcher)
        currentSearchResult = newResult
        return newResult
    }


    private val _searchList = MutableStateFlow<PagingData<AnimeMetaModel>>(PagingData.empty())
    val searchList = _searchList.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    val queryString = MutableStateFlow("")

    init {
        searchData()
    }

    private fun searchData() {
        viewModelScope.launch(ioDispatcher) {
            queryString.collect { query ->
                getSearchResultUseCase(query).cachedIn(viewModelScope).flowOn(ioDispatcher)
                    .collect {
                        _searchList.value = it
                    }
            }
        }
    }
}