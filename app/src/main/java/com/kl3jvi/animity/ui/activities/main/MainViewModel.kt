package com.kl3jvi.animity.ui.activities.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.domain.repositories.fragment_repositories.UserRepository
import com.kl3jvi.animity.domain.repositories.persistence_repositories.LocalStorage
import com.kl3jvi.animity.domain.use_cases.GetGogoKeysUseCase
import com.kl3jvi.animity.domain.use_cases.GetUserSessionUseCase
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.asResult
import com.kl3jvi.animity.utils.logError
import com.kl3jvi.animity.utils.logMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel @Inject constructor(
    private val userSession: GetUserSessionUseCase,
    private val getGogoKeys: GetGogoKeysUseCase,
    private val userRepository: UserRepository,
    private val localStorage: LocalStorage
) : ViewModel() {

    init {
        getUserSession()
        updateEncryptionKeys()
    }

    /* Used to notify the activity that the view model is ready to be used. */
    val initialise = MutableLiveData<Unit>()

    /**
     * It gets the user session from the server and sets the user id in the repository.
     */
    private fun getUserSession() {
        viewModelScope.launch(Dispatchers.IO) {
            userSession().asResult().collect {
                when (it) {
                    is Result.Error -> {
                        logError(it.exception)
                    }
                    Result.Loading -> {

                    }
                    is Result.Success -> {
                        if (!it.data.hasErrors())
                            userRepository.setAniListUserId(it.data.data?.viewer?.id.toString())
                    }
                }
            }
        }
    }


    /**
     * > It gets the encryption keys from the server and saves them to the local storage
     */
    private fun updateEncryptionKeys() {
        viewModelScope.launch(Dispatchers.IO) {
            getGogoKeys().asResult().collect {
                when (it) {
                    is Result.Error -> {
                        logMessage(it.exception?.message)
                    }
                    Result.Loading -> {}
                    is Result.Success -> {
                        val data = it.data
                        localStorage.iv = data.iv
                        localStorage.key = data.key
                        localStorage.secondKey = data.secondKey
                    }
                }
            }
        }
    }
}