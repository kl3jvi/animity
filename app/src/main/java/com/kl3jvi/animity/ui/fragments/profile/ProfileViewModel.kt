package com.kl3jvi.animity.ui.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.mapper.ProfileData
import com.kl3jvi.animity.data.mapper.ProfileRow
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import com.kl3jvi.animity.domain.repositories.persistence_repositories.LocalStorage
import com.kl3jvi.animity.domain.use_cases.GetAnimeListForProfileUseCase
import com.kl3jvi.animity.domain.use_cases.GetUserDataUseCase
import com.kl3jvi.animity.domain.use_cases.GetUserSessionUseCase
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.logMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userSession: GetUserSessionUseCase,
    private val userData: GetUserDataUseCase,
    private val userRepositoryImpl: UserRepositoryImpl,
    private val animeListUseCase: GetAnimeListForProfileUseCase,
    private val dataStore: LocalStorage,
    private val profileUseCase: GetAnimeListForProfileUseCase
) : ViewModel() {

    fun clearStorage() {
        userRepositoryImpl.clearStorage()
    }

    private val _profileData = MutableStateFlow<ProfileData?>(null)
    val profileData = _profileData.asStateFlow()

    private val _animeList = MutableStateFlow<List<ProfileRow>?>(null)
    val animeList = _animeList.asStateFlow()

    init {
        getProfileData()
    }

    private fun getProfileData() {
        viewModelScope.launch(Dispatchers.IO) {
            val profileDeferred = async { userData(dataStore.aniListUserId?.toInt()) }
            val animeListDeferred = async { animeListUseCase(dataStore.aniListUserId?.toInt()) }

            val (profileData, animeList) = awaitAll(profileDeferred, animeListDeferred)



            profileData.collect {
                when (it) {
                    is NetworkResource.Failed -> {
                        logMessage(it.message)
                    }
                    is NetworkResource.Success -> {
                        _profileData.value = it.data as ProfileData
                    }
                }
            }

            animeList.collect {
                when (it) {
                    is NetworkResource.Failed -> {
                        logMessage(it.message)
                    }
                    is NetworkResource.Success -> {
                        _animeList.value = it.data as List<ProfileRow>
                    }
                }
            }
        }
    }
}

