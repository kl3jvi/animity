package com.kl3jvi.animity.data.datasource

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Optional
import com.kl3jvi.animity.SessionQuery
import com.kl3jvi.animity.UserQuery
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class UserDataSourceImpl @Inject constructor(
    private val apolloClient: ApolloClient
) : UserDataSource {
    override fun getSessionForUser(): Flow<ApolloResponse<SessionQuery.Data>> {
        return apolloClient.query(SessionQuery()).toFlow()
    }

    override fun getUserData(id: Int?): Flow<ApolloResponse<UserQuery.Data>> {
        return apolloClient.query(UserQuery(Optional.Present(id))).toFlow()
    }

}