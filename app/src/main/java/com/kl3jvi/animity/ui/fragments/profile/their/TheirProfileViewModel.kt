package com.kl3jvi.animity.ui.fragments.profile.their

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.domain.repositories.ProfileRepository
import com.kl3jvi.animity.utils.mapToUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@HiltViewModel
class TheirProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val theirProfileData = savedStateHandle.getStateFlow("userId", -1)
        .flatMapLatest { userId ->
            profileRepository.getProfileData(userId)
        }.mapToUiState(viewModelScope)
}
