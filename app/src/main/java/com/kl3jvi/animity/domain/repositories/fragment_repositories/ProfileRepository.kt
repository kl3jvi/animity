package com.kl3jvi.animity.domain.repositories.fragment_repositories

import com.kl3jvi.animity.data.mapper.ProfileData
import com.kl3jvi.animity.data.mapper.ProfileRow
import com.kl3jvi.animity.utils.NetworkResource
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfileData(userId: Int?): Flow<NetworkResource<ProfileData>>
    fun getProfileAnimes(userId: Int?): Flow<NetworkResource<List<ProfileRow>>>
}