package com.kl3jvi.animity.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kl3jvi.animity.domain.GetSearchResultUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getSearchResultUseCase: GetSearchResultUseCase
) : ViewModel() {

    private val _query = MutableLiveData<String>()

    val searchResult = Transformations.switchMap(_query) { query ->
        getSearchResultUseCase.getSearchData(query).asLiveData()
    }

    fun passQuery(query: String) {
        _query.value = query
    }

}