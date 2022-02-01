package com.kl3jvi.animity.domain.use_cases

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFavoriteAnimesUseCase @Inject constructor(
    private val userRepository: UserRepositoryImpl
) {
    operator fun invoke(userId: Int?, page: Int?): Flow<ApolloResponse<FavoritesAnimeQuery.Data>> {
        return userRepository.getFavoriteAnimes(userId, page)
    }
}