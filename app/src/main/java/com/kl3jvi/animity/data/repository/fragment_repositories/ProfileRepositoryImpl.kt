package com.kl3jvi.animity.data.repository.fragment_repositories

import com.kl3jvi.animity.data.mapper.ProfileData
import com.kl3jvi.animity.data.mapper.ProfileRow
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.fragment_repositories.ProfileRepository
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.logError
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiClient: GogoAnimeApiClient
) : ProfileRepository {
    override fun getProfileData(userId: Int?): Flow<NetworkResource<ProfileData>> {
        return try {
            apiClient.getProfileData(userId).catch { }.mapNotNull {
                NetworkResource.Success(it.data?.convert() ?: ProfileData())
            }
        } catch (e: Exception) {
            logError(e)
            flowOf(NetworkResource.Failed(e.localizedMessage ?: "Error Occurred!"))
        }
    }

    override fun getProfileAnimes(userId: Int?): Flow<NetworkResource<List<ProfileRow>>> {
        return try {
            apiClient.getAnimeListData(userId).mapNotNull {
                NetworkResource.Success(it.data?.convert()?: listOf())
            }
        } catch (e: Exception) {
            logError(e)
            flowOf(NetworkResource.Failed(e.localizedMessage ?: "Error Occurred!"))
        }
    }
}











