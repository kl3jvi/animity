package com.kl3jvi.animity.data.repository.fragment_repositories

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.AnimeListCollectionQuery
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.data.mapper.ProfileData
import com.kl3jvi.animity.data.mapper.ProfileRow
import com.kl3jvi.animity.data.mapper.convert
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.domain.repositories.fragment_repositories.ProfileRepository
import com.kl3jvi.animity.utils.logError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class ProfileRepositoryImpl @Inject constructor(
    private val aniListGraphQlClient: AniListGraphQlClient
) : ProfileRepository {
    override fun getProfileData(userId: Int?): Flow<ProfileData> {
        return aniListGraphQlClient.getProfileData(userId).catch { e -> logError(e) }
            .mapNotNull(ApolloResponse<UserQuery.Data>::convert)
    }

    override fun getProfileAnimes(userId: Int?): Flow<List<ProfileRow>> {
        return aniListGraphQlClient.getAnimeListData(userId)
            .mapNotNull(ApolloResponse<AnimeListCollectionQuery.Data>::convert)
    }
}











