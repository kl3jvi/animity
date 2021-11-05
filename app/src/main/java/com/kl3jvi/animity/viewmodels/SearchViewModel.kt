package com.kl3jvi.animity.viewmodels

import androidx.lifecycle.*
import com.kl3jvi.animity.domain.GetAnimeDetailsUseCase
import com.kl3jvi.animity.domain.GetSearchResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
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