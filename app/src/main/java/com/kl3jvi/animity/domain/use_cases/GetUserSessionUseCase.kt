package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetUserSessionUseCase @Inject constructor(
    private val user: UserRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke() = user.getSessionForUser().flowOn(ioDispatcher)
}