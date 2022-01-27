package com.kl3jvi.animity.data.datasource

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.SessionQuery

interface UserDataSource {
    suspend fun getSessionForUser(): ApolloResponse<SessionQuery.Data>
}