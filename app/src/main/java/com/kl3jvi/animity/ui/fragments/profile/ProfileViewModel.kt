package com.kl3jvi.animity.ui.fragments.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.SessionQuery
import com.kl3jvi.animity.domain.use_cases.GetUserSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userSession: GetUserSessionUseCase
) : ViewModel() {
    private val _userData = MutableLiveData<SessionQuery.Data?>()
    val userData: LiveData<SessionQuery.Data?> = _userData
    init {
        getUserSession()
    }
    fun getUserSession() {
        viewModelScope.launch {
            userSession().also { _userData.value = it }
        }
    }
}