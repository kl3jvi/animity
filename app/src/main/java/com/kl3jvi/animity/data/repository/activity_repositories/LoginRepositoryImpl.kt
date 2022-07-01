package com.kl3jvi.animity.data.repository.activity_repositories

import com.kl3jvi.animity.data.network.anilist_service.AniListClient
import com.kl3jvi.animity.domain.repositories.activity_repositories.LoginRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject


class LoginRepositoryImpl @Inject constructor(
    private val aniListClient: AniListClient,
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
            aniListClient.getAccessToken(
                grantType,
                clientId,
                clientSecret,
                redirectUri,
                code
            )
        )
    }.flowOn(ioDispatcher)
}