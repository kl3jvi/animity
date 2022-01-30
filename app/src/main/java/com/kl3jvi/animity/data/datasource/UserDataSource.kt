package com.kl3jvi.animity.data.datasource

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.SessionQuery
import com.kl3jvi.animity.UserQuery
import kotlinx.coroutines.flow.Flow

interface UserDataSource {
    fun getSessionForUser(): Flow<ApolloResponse<SessionQuery.Data>>
    fun getUserData(id: Int?): Flow<ApolloResponse<UserQuery.Data>>
}