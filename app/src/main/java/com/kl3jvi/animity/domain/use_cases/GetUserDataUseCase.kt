package com.kl3jvi.animity.domain.use_cases

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val user: UserRepositoryImpl
) {
    operator fun invoke(id: Int?): Flow<ApolloResponse<UserQuery.Data>> {
        return user.getUserData(id)
    }
}