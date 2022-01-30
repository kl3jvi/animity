package com.kl3jvi.animity.ui.fragments.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import com.kl3jvi.animity.domain.use_cases.GetAnimeListForProfileUseCase
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
    private val userData: GetUserDataUseCase,
    private val userRepositoryImpl: UserRepositoryImpl,
    private val animeListUseCase: GetAnimeListForProfileUseCase
) : ViewModel() {
    fun clearStorage() {
        userRepositoryImpl.clearStorage()
    }


    val profileData = userSession().flatMapLatest {
        userData(it.data?.viewer?.id)
    }.asLiveData()

    val animeList = userSession().flatMapLatest {
        animeListUseCase(it.data?.viewer?.id)
    }.asLiveData()
}