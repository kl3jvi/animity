package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.repository.LoginRepositoryImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetAccessTokenUseCase @Inject constructor(
    private val repository: LoginRepositoryImpl
) {
    operator fun invoke(
        authenticationCode: String,
        clientId: String,
        clientSecret: String,
        redirectUri: String,
        code: String
    ) = repository.getAccessToken(
        authenticationCode,
        clientId,
        clientSecret,
        redirectUri,
        code
    )
}