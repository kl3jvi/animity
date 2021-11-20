package com.kl3jvi.animity.ui.fragments.search

import androidx.lifecycle.*
import com.kl3jvi.animity.domain.GetSearchResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchResultUseCase: GetSearchResultUseCase,
) : ViewModel() {

    private val _query = MutableLiveData<String>()

    val searchResult = Transformations.switchMap(_query) { query ->
        getSearchResultUseCase.getSearchData(query)
            .asLiveData(Dispatchers.IO + viewModelScope.coroutineContext)
    }

    fun passQuery(query: String) {
        _query.value = query
    }
}
