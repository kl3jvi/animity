package com.kl3jvi.animity.domain.use_cases

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.data.datasource.UserDataSourceImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val user: UserDataSourceImpl
) {
    operator fun invoke(id: Int?): Flow<ApolloResponse<UserQuery.Data>> {
        return user.getUserData(id)
    }
}