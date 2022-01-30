package com.kl3jvi.animity.ui.fragments.profile

import androidx.lifecycle.ViewModel
import com.kl3jvi.animity.domain.use_cases.GetUserDataUseCase
import com.kl3jvi.animity.domain.use_cases.GetUserSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flatMapLatest
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userSession: GetUserSessionUseCase,
    private val userData: GetUserDataUseCase
) : ViewModel() {
    val profileData = userSession().flatMapLatest {
        userData(it.data?.viewer?.id)
    }
}