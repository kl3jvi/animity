package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.model.auth_models.AuthResponse
import com.kl3jvi.animity.data.network.anilist_service.AniListClient
import com.kl3jvi.animity.domain.repositories.activity_repositories.LoginRepository
import com.kl3jvi.animity.domain.repositories.network_repositories.NetworkBoundRepository
import com.kl3jvi.animity.utils.NetworkResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoginRepositoryImpl @Inject constructor(
    private val aniListClient: AniListClient
) : LoginRepository {

    override fun getAccessToken(
        grantType: String,
        clientId: Int,
        clientSecret: String,
        redirectUri: String,
        code: String
    ): Flow<NetworkResource<AuthResponse>> {
        return object : NetworkBoundRepository<AuthResponse>() {
            override suspend fun fetchFromRemote(): Response<AuthResponse> {
                return aniListClient.getAccessToken(
                    grantType,
                    clientId,
                    clientSecret,
                    redirectUri,
                    code
                )
            }
        }.asFlow()
    }
}