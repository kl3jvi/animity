package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.repository.fragment_repositories.FavoriteRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetFavoriteAnimesUseCase @Inject constructor(
    private val favoriteRepositoryImpl: FavoriteRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(userId: Int?, page: Int?) =
        favoriteRepositoryImpl.getFavoriteAnimesFromAniList(userId, page)
            .flowOn(ioDispatcher)
}