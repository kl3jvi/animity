package com.kl3jvi.animity.domain.use_cases

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.UserQuery
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val user: UserRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(id: Int?): Flow<ApolloResponse<UserQuery.Data>> {
        return user.getUserData(id).flowOn(ioDispatcher)
    }
}