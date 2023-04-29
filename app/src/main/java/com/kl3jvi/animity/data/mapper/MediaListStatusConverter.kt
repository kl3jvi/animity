package com.kl3jvi.animity.data.mapper

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.SaveMediaMutation
import com.kl3jvi.animity.data.model.ui_models.ChangedMediaResponse
import java.util.Locale

fun ApolloResponse<SaveMediaMutation.Data>.convert(): ChangedMediaResponse {
    return ChangedMediaResponse(
        this.data?.saveMedia?.id,
        MediaStatusAnimity.stringToMediaListStatus(this.data?.saveMedia?.status?.rawValue)
    )
}

enum class MediaStatusAnimity {
    COMPLETED,
    WATCHING,
    DROPPED,
    PAUSED,
    PLANNING,
    REPEATING,
    NOTHING;

    companion object {
        fun stringToMediaListStatus(passedString: String?): MediaStatusAnimity {
            return when (passedString?.uppercase(Locale.getDefault())) {
                "COMPLETED" -> COMPLETED
                "CURRENT" -> WATCHING
                "DROPPED" -> DROPPED
                "PAUSED" -> PAUSED
                "PLANNING" -> PLANNING
                "REPEATING" -> REPEATING
                else -> NOTHING
            }
        }
    }
}
