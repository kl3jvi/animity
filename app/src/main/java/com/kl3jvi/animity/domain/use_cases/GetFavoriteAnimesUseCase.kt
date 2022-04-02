package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.model.ui_models.Media
import com.kl3jvi.animity.data.repository.fragment_repositories.FavoriteRepositoryImpl
import com.kl3jvi.animity.utils.NetworkResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

@ExperimentalCoroutinesApi
class GetFavoriteAnimesUseCase @Inject constructor(
    private val favoriteRepositoryImpl: FavoriteRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(userId: Int?, page: Int?): Flow<NetworkResource<List<Media>>> {
        return favoriteRepositoryImpl.getFavoriteAnimesFromAniList(userId, page)
            .flowOn(ioDispatcher)
    }
}