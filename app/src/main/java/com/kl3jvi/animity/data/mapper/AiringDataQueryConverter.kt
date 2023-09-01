package com.kl3jvi.animity.data.mapper

import android.os.Parcelable
import android.text.format.DateUtils
import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.AiringQuery
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import kotlinx.parcelize.Parcelize

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
        val timeInMillis = airsAt?.times(1000L) ?: System.currentTimeMillis()

        // If the time is in the past
        if (timeInMillis <= System.currentTimeMillis()) {
            return "Aired"
        }

        return DateUtils.getRelativeTimeSpanString(
            timeInMillis,
            System.currentTimeMillis(),
            DateUtils.HOUR_IN_MILLIS,
        ).toString()
    }

}
