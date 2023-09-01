package com.kl3jvi.animity.data.mapper

import android.os.Parcelable
import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.AiringQuery
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun ApolloResponse<AiringQuery.Data>.convert(): List<AiringInfo> {
    return this.data?.Page?.airingSchedules?.map {
        AiringInfo(
            airsAt = it?.airingAt,
            media = it?.media?.homeMedia?.convert(),
            episode = it?.episode,
        )
    }?.filter {
        it.media?.countryOfOrigin != "CN"
    } ?: emptyList()
}

@Parcelize
data class AiringInfo(
    val airsAt: Int?,
    val media: AniListMedia? = AniListMedia(),
    val episode: Int?,
) : Parcelable {
    fun getEpisodeTitle() = "Episode $episode"

    fun time(): String {
        val date = Date(
            airsAt?.times(1000L) ?: System.currentTimeMillis(),
        ) // epoch is in seconds, but Java expects milliseconds
        val format = SimpleDateFormat("HH:mm", Locale.getDefault())
        return format.format(date)
    }
}
