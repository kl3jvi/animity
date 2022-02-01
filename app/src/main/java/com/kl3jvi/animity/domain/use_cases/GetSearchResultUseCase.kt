package com.kl3jvi.animity.domain.use_cases

import androidx.paging.PagingData
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.data.repository.fragment_repositories.SearchRepositoryImpl
import com.kl3jvi.animity.utils.Constants
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
    operator fun invoke(searchQuery: String): Flow<PagingData<AnimeMetaModel>> {
        return searchRepository.fetchSearchData(
            Constants.getHeader(),
            searchQuery
        ).flowOn(ioDispatcher)
    }
}
