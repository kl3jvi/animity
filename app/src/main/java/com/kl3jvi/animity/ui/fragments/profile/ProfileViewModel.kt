package com.kl3jvi.animity.ui.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import com.kl3jvi.animity.domain.repositories.persistence_repositories.LocalStorage
import com.kl3jvi.animity.domain.use_cases.GetAnimeListForProfileUseCase
import com.kl3jvi.animity.domain.use_cases.GetUserDataUseCase
import com.kl3jvi.animity.domain.use_cases.GetUserSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userSession: GetUserSessionUseCase,
    private val userData: GetUserDataUseCase,
    private val userRepositoryImpl: UserRepositoryImpl,
    private val animeListUseCase: GetAnimeListForProfileUseCase,
    private val dataStore: LocalStorage
) : ViewModel() {

    fun clearStorage() {
        userRepositoryImpl.clearStorage()
    }

    private val _profileData = MutableStateFlow<ApolloResponse<UserQuery.Data>?>(null)
    val profileData = _profileData.asStateFlow()

    private val _animeList = MutableStateFlow<ApolloResponse<AnimeListCollectionQuery.Data>?>(null)
    val animeList = _animeList.asStateFlow()

    init {
        getProfileData()
    }

    private fun getProfileData() {
        viewModelScope.launch(Dispatchers.IO) {
            val profileDeferred =
                async { userData(dataStore.aniListUserId?.toInt()) }
            val animeListDeferred = async {
                animeListUseCase(dataStore.aniListUserId?.toInt())
            }

            profileDeferred.await().collect {
                if (!it.hasErrors())
                    _profileData.value = it
            }

            animeListDeferred.await().collect {
                if (!it.hasErrors())
                    _animeList.value = it
            }

        }
    }
}