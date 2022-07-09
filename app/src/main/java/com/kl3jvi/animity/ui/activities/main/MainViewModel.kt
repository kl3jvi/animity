package com.kl3jvi.animity.ui.activities.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.domain.repositories.HomeRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.domain.repositories.UserRepository
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
    private val homeRepository: HomeRepository,
    private val userRepository: UserRepository,
    private val localStorage: PersistenceRepository
) : ViewModel() {

    val initialise = MutableLiveData<Unit>()

    init {
        getUserSession()
        updateEncryptionKeys()
    }


    /**
     * It gets the user session from the server and sets the user id in the repository.
     */
    private fun getUserSession() {
        viewModelScope.launch(Dispatchers.IO) {
            userRepository.getSessionForUser().asResult().collect {
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
            homeRepository.getEncryptionKeys().asResult().collect {
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