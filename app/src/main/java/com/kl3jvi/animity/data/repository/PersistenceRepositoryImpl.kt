package com.kl3jvi.animity.data.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.kl3jvi.animity.data.model.ui_models.EpisodeEntity
import com.kl3jvi.animity.data.model.ui_models.LocalAnime
import com.kl3jvi.animity.data.model.ui_models.LocalEpisode
import com.kl3jvi.animity.domain.repositories.PersistenceRepository
import com.kl3jvi.animity.persistence.EpisodeDao
import com.kl3jvi.animity.persistence.LocalDownloadsDao
import com.kl3jvi.animity.persistence.LocalEpisodeDao
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow

class PersistenceRepositoryImpl
@Inject
constructor(
    private val episodeDao: EpisodeDao,
    private val sharedPreferences: SharedPreferences,
    private val localEpisodeDao: LocalEpisodeDao,
    private val localAnimeDao: LocalDownloadsDao,
) : PersistenceRepository {
    override suspend fun insertEpisode(episodeEntity: EpisodeEntity) =
        episodeDao.insertEpisode(episodeEntity)

    override suspend fun updateEpisode(episodeEntity: EpisodeEntity) =
        episodeDao.updateEpisode(episodeEntity)

    override suspend fun getEpisodeContent(episodeUrl: String): Flow<EpisodeEntity> =
        episodeDao.getEpisodeContent(episodeUrl)

    override suspend fun isEpisodeOnDatabase(episodeUrl: String): Boolean =
        episodeDao.isEpisodeOnDatabase(episodeUrl)

    override suspend fun insertLocalEpisode(localEpisode: LocalEpisode) =
        localEpisodeDao.insert(localEpisode)

    override fun getDownloadedEpisodesForAnime(animeId: Int) =
        localEpisodeDao.getEpisodesForAnime(animeId)

    override suspend fun incrementDownloadedEpisodesCount(animeId: Int) =
        localAnimeDao.incrementDownloadedEpisodesCount(animeId)

    override suspend fun insertLocalAnime(localAnime: LocalAnime) = localAnimeDao.insert(localAnime)

    override suspend fun getLocalAnimeById(aniListId: Int): LocalAnime? =
        localAnimeDao.getAnimeById(aniListId)

    override fun getAllAnimesThatHasDownloadedEpisodes() = localAnimeDao.getAllAnime()

    companion object {
        private const val BEARER_TOKEN = "bearerToken"
        private const val EXPIRATION = "expiration"
        private const val REFRESH_TOKEN = "refreshToken"
        private const val GUEST_TOKEN = "guestToken"
        private const val ANILIST_USER_ID = "anilistUserId"
        const val SELECTED_DNS = "dns"

        private const val ORIGIN = "https://gogoanime.gg/"
        private const val REFERER = "https://goload.pro/"
        private const val BASE_URL = "https://gogoanime.gg"
    }

    override var expiration: Int?
        get() = getDataInt(EXPIRATION)
        set(value) {
            setDataInt(EXPIRATION, value ?: -1)
        }

    override var bearerToken: String?
        get() = getData(BEARER_TOKEN)
        set(value) {
            setData(BEARER_TOKEN, value)
        }

    override var refreshToken: String?
        get() = getData(REFRESH_TOKEN)
        set(value) {
            setData(REFRESH_TOKEN, value)
        }

    override var guestToken: String?
        get() = getData(GUEST_TOKEN)
        set(value) {
            setData(GUEST_TOKEN, value)
        }

    override var aniListUserId: String?
        get() = getData(ANILIST_USER_ID)
        set(value) {
            setData(ANILIST_USER_ID, value)
        }

    override var dns: Int?
        get() = getData(SELECTED_DNS)?.toInt()
        set(value) {
            setData(SELECTED_DNS, value.toString())
        }

    override var iv: String?
        get() = getData(ORIGIN)
        set(value) {
            setData(ORIGIN, value)
        }

    override var key: String?
        get() = getData(REFERER)
        set(value) {
            setData(REFERER, value)
        }

    override var secondKey: String?
        get() = getData(BASE_URL)
        set(value) {
            setData(BASE_URL, value)
        }

    private fun getData(key: String): String? {
        return sharedPreferences.getString(key, null)
    }

    private fun getDataInt(key: String): Int? {
        return sharedPreferences.getInt(key, -1)
    }

    private fun setData(
        key: String,
        value: String?,
    ) {
        sharedPreferences.edit().putString(key, value).apply()
    }

    private fun setDataInt(
        key: String,
        value: Int,
    ) {
        sharedPreferences.edit().putInt(key, value).apply()
    }

    override fun clearStorage(triggered: () -> Unit) {
        sharedPreferences.edit {
            clear()
            triggered()
        }
    }

    inline fun <reified T> String?.fromJson(): T {
        return Gson().fromJson(this.orEmpty(), T::class.java)
    }

    inline fun <reified T> T?.toJson(): String? {
        return Gson().toJson(this)
    }
}
