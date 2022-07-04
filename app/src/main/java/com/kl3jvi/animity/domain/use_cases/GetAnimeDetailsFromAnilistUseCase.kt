package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetAnimeDetailsFromAnilistUseCase @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
)