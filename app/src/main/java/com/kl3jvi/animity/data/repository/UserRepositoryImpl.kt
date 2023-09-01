package com.kl3jvi.animity.data.repository

import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.AniListMediaEntity
import com.kl3jvi.animity.data.model.ui_models.mapToEntity
import com.kl3jvi.animity.data.network.anilist_service.AniListGraphQlClient
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.domain.repositories.UserRepository
import com.kl3jvi.animity.persistence.ScheduleDao
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.withContext
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val storage: PersistenceRepository,
    private val aniListGraphQlClient: AniListGraphQlClient,
    private val ioDispatcher: CoroutineDispatcher,
    private val scheduleDao: ScheduleDao,
) : UserRepository {

    override val bearerToken: String?
        get() = storage.bearerToken

    override val refreshToken: String?
        get() = storage.refreshToken

    override val guestToken: String?
        get() = storage.guestToken

    override val isAuthenticated: Boolean
        get() = storage.bearerToken != null

    override val isGuest: Boolean
        get() = storage.guestToken != null

    override val userId: String?
        get() = storage.aniListUserId

    override val expiration: Int?
        get() = storage.expiration

    override fun setBearerToken(authToken: String?) {
        storage.bearerToken = authToken
    }

    override fun setRefreshToken(refreshToken: String?) {
        storage.refreshToken = refreshToken
    }

    override fun setAniListUserId(sync: String?) {
        storage.aniListUserId = sync
    }

    override fun setExpirationTime(expiration: Int) {
        storage.expiration = expiration
    }

    override fun setProvider(provider: String) {
    }

    override fun clearStorage(triggered: () -> Unit) {
        storage.clearStorage(triggered)
    }

    override fun getSessionForUser() = flow {
        emit(aniListGraphQlClient.getSessionForUser())
    }.flowOn(ioDispatcher)

    override fun markAnimeAsFavorite(idAniList: Int?) = flow {
        emit(aniListGraphQlClient.markAnimeAsFavorite(idAniList))
    }.flowOn(ioDispatcher)

    override suspend fun markAnimeForSchedule(aniListMedia: AniListMedia) {
        val media = aniListMedia.mapToEntity()
        withContext(ioDispatcher) {
            if (scheduleDao.isAnimeOnDatabase(aniListMedia.idAniList)) {
                scheduleDao.deleteAnime(media)
            } else {
                scheduleDao.insertAnime(media)
            }
        }
    }

    override fun getScheduleStatusLocally(idAniList: Int): Flow<AniListMediaEntity?> {
        return scheduleDao.getAnimeScheduleStatus(idAniList)
    }
}
