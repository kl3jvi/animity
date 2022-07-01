package com.kl3jvi.animity.data.repository.fragment_repositories

import com.kl3jvi.animity.data.mapper.ProfileData
import com.kl3jvi.animity.data.mapper.ProfileRow
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.domain.repositories.fragment_repositories.ProfileRepository
import com.kl3jvi.animity.utils.logError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val apiClient: GogoAnimeApiClient
) : ProfileRepository {
    override fun getProfileData(userId: Int?): Flow<ProfileData> {
        return apiClient.getProfileData(userId).catch { e -> logError(e) }.mapNotNull {
            it.data?.convert() ?: ProfileData()
        }
    }

    override fun getProfileAnimes(userId: Int?): Flow<List<ProfileRow>> {
        return apiClient.getAnimeListData(userId).mapNotNull {
            it.data?.convert() ?: listOf()
        }
    }
}











