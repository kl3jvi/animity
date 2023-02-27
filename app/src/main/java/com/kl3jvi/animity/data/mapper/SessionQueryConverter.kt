package com.kl3jvi.animity.data.mapper

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.SessionQuery
import com.kl3jvi.animity.data.model.ui_models.SessionData

fun ApolloResponse<SessionQuery.Data>.convert(): SessionData? {
    return this.data?.viewer?.id?.let { SessionData(it) }
}
