package com.kl3jvi.animity.ui.fragments.profile

import androidx.lifecycle.ViewModel
import com.kl3jvi.animity.data.mapper.ProfileData
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.domain.repositories.ProfileRepository
import com.kl3jvi.animity.domain.repositories.UserRepository
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.asResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    profileRepository: ProfileRepository,
    localStorage: PersistenceRepository
) : ViewModel() {


    val profileData = profileRepository.getProfileData(localStorage.aniListUserId?.toInt())
        .asResult().map {
            when (it) {
                is Result.Error -> ProfileDataUiState.Error(it.exception)
                Result.Loading -> ProfileDataUiState.Loading
                is Result.Success -> ProfileDataUiState.Success(it.data)
            }
        }


    fun clearStorage() {
        userRepository.clearStorage()
    }
}

sealed interface ProfileDataUiState {
    data class Success(val data: ProfileData) : ProfileDataUiState
    object Loading : ProfileDataUiState
    data class Error(val exception: Throwable?) : ProfileDataUiState
}

