package com.kl3jvi.animity.domain.use_cases

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.SessionQuery
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import com.kl3jvi.animity.utils.logError
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetUserSessionUseCase @Inject constructor(
    private val user: UserRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<ApolloResponse<SessionQuery.Data>> {
        return try {
            user.getSessionForUser().flowOn(ioDispatcher)
        } catch (e: Exception) {
            e.printStackTrace()
            user.getSessionForUser().catch { e -> logError(e) }
        }
    }
}