package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.data.model.ui_models.ProfileData
import kotlinx.coroutines.flow.Flow

interface ProfileRepository {
    fun getProfileData(userId: Int?): Flow<ProfileData>
    fun followUser(userId: Int): Flow<Pair<String, String>>
}
