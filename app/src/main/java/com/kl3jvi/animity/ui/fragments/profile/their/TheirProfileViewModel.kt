package com.kl3jvi.animity.ui.fragments.profile.their

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.data.model.ui_models.User
import com.kl3jvi.animity.domain.repositories.ProfileRepository
import com.kl3jvi.animity.utils.mapToUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TheirProfileViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val savedStateHandle: SavedStateHandle,
) : ViewModel() {
    private val followState: MutableStateFlow<Pair<String, String>> = MutableStateFlow(Pair("Follow", ""))

    val theirProfileData = savedStateHandle.getStateFlow("user", User())
        .flatMapLatest { user ->
            profileRepository.getProfileData(user.id)
        }.combine(followState) { profileData, followState ->
            profileData.copy(
                userData = profileData.userData,
                profileRow = profileData.profileRow,
                followState = followState,
            )
        }.mapToUiState(viewModelScope)

    fun followUser() {
        val id = savedStateHandle.get<User>("user")?.id ?: return
        viewModelScope.launch {
            profileRepository.followUser(id).collect { newFollowState ->
                followState.value = newFollowState
            }
        }
    }
}
