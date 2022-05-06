package com.kl3jvi.animity.ui.activities.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.domain.repositories.fragment_repositories.UserRepository
import com.kl3jvi.animity.domain.repositories.persistence_repositories.LocalStorage
import com.kl3jvi.animity.domain.use_cases.GetGogoKeysUseCase
import com.kl3jvi.animity.domain.use_cases.GetUserSessionUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel
@Inject constructor(
    private val userSession: GetUserSessionUseCase,
    private val getGogoKeys: GetGogoKeysUseCase,
    private val userRepository: UserRepository,
    private val localStorage: LocalStorage
) : ViewModel() {

    init {
        getUserSession()
        updateUserKeys()
    }

    val initialise = MutableLiveData<Unit>()

    /**
     * It gets the user session from the server and sets the user id in the repository.
     */
    private fun getUserSession() {
        viewModelScope.launch(Dispatchers.IO) {
            userSession().collect {
                if (!it.hasErrors())
                    userRepository.setAniListUserId(it.data?.viewer?.id.toString())
            }
        }
    }


    private fun updateUserKeys() {
        viewModelScope.launch(Dispatchers.IO) {
            val iv = getGogoKeys().iv
            val key = getGogoKeys().key
            val secondKey = getGogoKeys().secondKey

            localStorage.iv = iv
            localStorage.key = key
            localStorage.secondKey = secondKey
        }
    }
}