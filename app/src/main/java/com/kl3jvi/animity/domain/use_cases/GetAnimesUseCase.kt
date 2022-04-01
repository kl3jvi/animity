package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.domain.repositories.fragment_repositories.HomeRepository
import com.kl3jvi.animity.utils.NetworkResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAnimesUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<NetworkResource<HomeData>> {
        return homeRepository.getHomeData().flowOn(ioDispatcher)
    }
}
