package com.kl3jvi.animity.ui.fragments.profile.my

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.domain.repositories.ProfileRepository
import com.kl3jvi.animity.domain.repositories.UserRepository
import com.kl3jvi.animity.utils.mapToUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.plus
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    profileRepository: ProfileRepository,
    localStorage: PersistenceRepository,
    ioDispatcher: CoroutineDispatcher
) : ViewModel() {

    val profileData = profileRepository
        .getProfileData(localStorage.aniListUserId?.toInt())
        .mapToUiState(viewModelScope + ioDispatcher)

    fun clearStorage() = userRepository.clearStorage()
}
