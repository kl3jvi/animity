package com.kl3jvi.animity.ui.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.mapper.ProfileData
import com.kl3jvi.animity.data.mapper.ProfileRow
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.domain.repositories.ProfileRepository
import com.kl3jvi.animity.domain.repositories.UserRepository
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.asResult
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
    private val localStorage: PersistenceRepository
) : ViewModel() {

    private val _profileData = MutableStateFlow(ProfileData())
    val profileData = _profileData.asStateFlow()

    private val _animeList = MutableStateFlow<List<ProfileRow>>(emptyList())
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
                if (it is Result.Success) {
                    _profileData.value = it.data as ProfileData
                } else {
                    return@collect
                }
            }

            animeList.asResult().collect {
                if (it is Result.Success) {
                    _animeList.value = it.data as List<ProfileRow>
                } else {
                    return@collect
                }
            }
        }
    }

    fun clearStorage() {
        userRepository.clearStorage()
    }
}
