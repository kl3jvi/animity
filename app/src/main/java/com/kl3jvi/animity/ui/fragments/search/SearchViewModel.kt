package com.kl3jvi.animity.ui.fragments.search

import androidx.lifecycle.*
import com.kl3jvi.animity.domain.use_cases.GetSearchResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchResultUseCase: GetSearchResultUseCase,
    private val ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _query = MutableLiveData<String>()
    val searchResult = Transformations.switchMap(_query) { query ->
        getSearchResultUseCase(query).asLiveData(ioDispatcher + viewModelScope.coroutineContext)
    }

    fun passQuery(query: String) {
        _query.value = query
    }
}