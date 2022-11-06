package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.data.mapper.ProfileData
import com.kl3jvi.animity.data.mapper.ProfileRow
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfileData(userId: Int?): Flow<ProfileData>
    fun getProfileAnimes(userId: Int?): Flow<List<ProfileRow>>
}
