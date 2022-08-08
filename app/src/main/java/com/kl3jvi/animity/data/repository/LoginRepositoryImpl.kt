package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.network.anilist_service.AuthClient
import com.kl3jvi.animity.domain.repositories.LoginRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class LoginRepositoryImpl @Inject constructor(
    private val authClient: AuthClient,
    private val ioDispatcher: CoroutineDispatcher
) : LoginRepository {
    override fun getAccessToken(
        grantType: String,
        clientId: Int,
        clientSecret: String,
        redirectUri: String,
        code: String
    ) = flow {
        emit(
            authClient.getAccessToken(
                grantType,
                clientId,
                clientSecret,
                redirectUri,
                code
            )
        )
    }.flowOn(ioDispatcher)
}