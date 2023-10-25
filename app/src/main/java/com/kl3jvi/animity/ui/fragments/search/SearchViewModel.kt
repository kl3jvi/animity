package com.kl3jvi.animity.ui.fragments.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.kl3jvi.animity.data.enums.SearchMode
import com.kl3jvi.animity.data.enums.SortType
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.User
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.domain.repositories.SearchRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

@HiltViewModel
class SearchViewModel
@Inject
constructor(
    private val searchRepository: SearchRepository,
    localRepository: PersistenceRepository,
    ioDispatcher: CoroutineDispatcher,
) : ViewModel() {
    val myId = localRepository.aniListUserId ?: "-1"

    private val _currentSearchMode = MutableStateFlow(SearchMode.ANIME)
    val currentSearchMode: StateFlow<SearchMode> = _currentSearchMode.asStateFlow()

    private val _searchList = MutableStateFlow<PagingData<AniListMedia>>(PagingData.empty())
    val searchList: StateFlow<PagingData<AniListMedia>> = _searchList.asStateFlow()

    private val _usersList = MutableStateFlow<PagingData<User>>(PagingData.empty())
    val usersList: StateFlow<PagingData<User>> = _usersList.asStateFlow()

    private val sortTypes = mutableListOf<SortType>()
    private var lastSearchQuery = ""

    // Channels and Flows for Anime and User search
    private val animeSearchQueryChannel = Channel<SearchQuery>(Channel.UNLIMITED)
    private val animeSearchFlow =
        animeSearchQueryChannel
            .receiveAsFlow()
            .debounce(500)
            .filter { it.query.length >= 2 }
            .flatMapLatest { searchRepository.fetchAniListSearchData(it.query, it.sortTypes) }
            .cachedIn(viewModelScope)
            .flowOn(ioDispatcher)

    private val userSearchQueryChannel = Channel<String>(Channel.UNLIMITED)
    private val userSearchFlow =
        userSearchQueryChannel
            .receiveAsFlow()
            .debounce(500)
            .filter { it.length >= 2 }
            .flatMapLatest { searchRepository.fetchAniListUsers(it) }
            .cachedIn(viewModelScope)
            .flowOn(ioDispatcher)

    init {
        viewModelScope.launch {
            animeSearchFlow.collectLatest {
                _searchList.value = it
            }
        }

        viewModelScope.launch {
            userSearchFlow.collectLatest {
                _usersList.value = it
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        lastSearchQuery = query.trim()
        when (_currentSearchMode.value) {
            SearchMode.ANIME -> searchForAnime(lastSearchQuery)
            SearchMode.USERS -> searchForUsers(lastSearchQuery)
        }
    }

    private fun searchForAnime(query: String) {
        animeSearchQueryChannel.trySend(SearchQuery(query, sortTypes)).isSuccess
    }

    private fun searchForUsers(query: String) {
        userSearchQueryChannel.trySend(query).isSuccess
    }

    fun toggleSortType(sortType: SortType) {
        if (sortTypes.contains(sortType)) {
            sortTypes.remove(sortType)
        } else {
            sortTypes.add(sortType)
        }
        onSearchQueryChanged(lastSearchQuery)
    }

    fun switchToAnimeSearch() {
        _currentSearchMode.value = SearchMode.ANIME
    }

    fun switchToUserSearch() {
        _currentSearchMode.value = SearchMode.USERS
    }
}

data class SearchQuery(
    val query: String,
    val sortTypes: MutableList<SortType>,
)
