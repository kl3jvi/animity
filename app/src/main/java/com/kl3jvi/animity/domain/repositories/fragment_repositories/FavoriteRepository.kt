package com.kl3jvi.animity.domain.repositories.fragment_repositories

import com.kl3jvi.animity.data.model.ui_models.test.DetailedAnimeInfo

interface FavoriteRepository {
    suspend fun getGogoUrlFromAniListId(id: String): DetailedAnimeInfo
}