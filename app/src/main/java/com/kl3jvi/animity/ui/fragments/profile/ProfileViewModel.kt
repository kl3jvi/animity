package com.kl3jvi.animity.ui.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.mapper.ProfileData
import com.kl3jvi.animity.data.mapper.ProfileRow
import com.kl3jvi.animity.domain.repositories.ProfileRepository
import com.kl3jvi.animity.domain.repositories.UserRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.asResult
import com.kl3jvi.animity.utils.logMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
    private val localStorage: PersistenceRepository,
) : ViewModel() {

    private val _profileData = MutableStateFlow<ProfileData?>(null)
    val profileData = _profileData.asStateFlow()

    private val _animeList = MutableStateFlow<List<ProfileRow>?>(null)
    val animeList = _animeList.asStateFlow()

    init {
        getProfileData()
    }

    private fun getProfileData() {
        viewModelScope.launch(Dispatchers.IO) {
            val profileDeferred =
                async { profileRepository.getProfileData(localStorage.aniListUserId?.toInt()) }
            val animeListDeferred =
                async { profileRepository.getProfileAnimes(localStorage.aniListUserId?.toInt()) }

            val (profileData, animeList) = awaitAll(profileDeferred, animeListDeferred)

            profileData.asResult().collect {
                when (it) {
                    is Result.Error -> {
                        logMessage(it.exception?.message)
                    }
                    Result.Loading -> {

                    }
                    is Result.Success -> _profileData.value = it.data as ProfileData
                }
            }

            animeList.asResult().collect {
                when (it) {
                    is Result.Error -> logMessage(it.exception?.message)
                    Result.Loading -> {}
                    is Result.Success -> _animeList.value = it.data as List<ProfileRow>
                }
            }
        }
    }

    fun clearStorage() {
        userRepository.clearStorage()
    }
}


