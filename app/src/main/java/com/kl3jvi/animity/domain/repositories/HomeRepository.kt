package com.kl3jvi.animity.domain.repositories

import com.kl3jvi.animity.data.model.ui_models.GogoAnimeKeys
import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.utils.parser.Parser
import kotlinx.coroutines.flow.Flow

interface HomeRepository {
    fun getHomeData(): Flow<HomeData>
    fun getEncryptionKeys(): Flow<GogoAnimeKeys>
}
