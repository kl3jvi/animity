package com.kl3jvi.animity.domain.use_cases

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.SessionQuery
import com.kl3jvi.animity.data.datasource.UserDataSourceImpl
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetUserSessionUseCase @Inject constructor(
    private val user: UserDataSourceImpl
) {
    operator fun invoke(): Flow<ApolloResponse<SessionQuery.Data>> {
        return user.getSessionForUser()
    }
}