package com.kl3jvi.animity.domain.use_cases

import androidx.paging.PagingData
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.repository.fragment_repositories.SearchRepositoryImpl
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSearchResultUseCase @Inject constructor(
    private val searchRepository: SearchRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher

) {
    operator fun invoke(searchQuery: String): Flow<PagingData<AniListMedia>> {
        return searchRepository.fetchAniListSearchData(searchQuery).flowOn(ioDispatcher)
    }
}
