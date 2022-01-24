package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.network.AniListClient
import com.kl3jvi.animity.domain.repositories.DataStoreManager
import com.kl3jvi.animity.domain.repositories.LoginRepository
import com.kl3jvi.animity.domain.repositories.NetworkBoundRepository
import com.kl3jvi.animity.utils.Resource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import retrofit2.Response
import javax.inject.Inject

@ExperimentalCoroutinesApi
class LoginRepositoryImpl @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val aniListClient: AniListClient
) : LoginRepository {

    override fun getAccessToken(
        code: String,
        clientId: String,
        clientSecret: String,
        redirectUrl: String
    ): Flow<Resource<String>> {
        return object : NetworkBoundRepository<String, String>() {
            override suspend fun saveRemoteData(response: String) =
                dataStoreManager.saveTokenToPreferencesStore(response)


            override fun fetchFromLocal(): Flow<String> =
                dataStoreManager.getTokenFromPreferencesStore()


            override suspend fun fetchFromRemote(): Response<String> {
//                aniListClient
            }

        }.asFlow()
    }
}