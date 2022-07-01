package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.domain.repositories.fragment_repositories.HomeRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetAnimesUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
    private val ioDispatcher: CoroutineDispatcher
) {
    /**
     * It returns a Flow of NetworkResource<HomeData> that is emitted from the
     * homeRepository.getHomeData() function
     *
     * @return A Flow of NetworkResource<HomeData>
     */
    operator fun invoke() = homeRepository.getHomeData().flowOn(ioDispatcher)

}
