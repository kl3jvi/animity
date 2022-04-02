package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.repository.activity_repositories.LoginRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetAccessTokenUseCase @Inject constructor(
    private val repository: LoginRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(
        grantType: String,
        clientId: Int,
        clientSecret: String,
        redirectUri: String,
        authorizationToken: String
    ) = repository.getAccessToken(
        grantType,
        clientId,
        clientSecret,
        redirectUri,
        authorizationToken
    ).flowOn(ioDispatcher)
}