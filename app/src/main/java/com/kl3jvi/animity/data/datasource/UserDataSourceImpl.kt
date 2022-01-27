package com.kl3jvi.animity.data.datasource

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.SessionQuery
import javax.inject.Inject

class UserDataSourceImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : UserDataSource {
    override suspend fun getSessionForUser(): ApolloResponse<SessionQuery.Data> {
        return apolloClient.query(SessionQuery()).execute()
    }
}