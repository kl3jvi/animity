package com.kl3jvi.animity.ui.activities.main

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.kl3jvi.animity.domain.repositories.HomeRepository
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.domain.repositories.UserRepository
import com.kl3jvi.animity.utils.network.NetworkMonitor
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@ExperimentalCoroutinesApi
@HiltViewModel
class MainViewModel
@Inject
constructor(
    private val homeRepository: HomeRepository,
    private val userRepository: UserRepository,
    private val localStorage: PersistenceRepository,
    private val ioDispatcher: CoroutineDispatcher,
    private val network: NetworkMonitor,
) : ViewModel() {

    init {
        viewModelScope.launch(ioDispatcher)
        {
            launch { getUserSession() }
            launch { updateEncryptionKeys() }
        }
    }

    private fun getUserSession() {
        userRepository.getSessionForUser()
            .onEach { data ->
                if (!data.hasErrors()) {
                    userRepository.setAniListUserId(data.data?.viewer?.id.toString())
                } else {
                    Log.e("MainViewModel", "Error getting user session")
                }
            }.launchIn(viewModelScope)
    }

    /**
     * > It gets the encryption keys from the server and saves them to the local storage
     */
    private fun updateEncryptionKeys() {
        homeRepository.getEncryptionKeys()
            .onEach { data ->
                with(localStorage) {
                    iv = data.iv
                    key = data.key
                    secondKey = data.secondKey
                }
            }.launchIn(viewModelScope)
    }
}
