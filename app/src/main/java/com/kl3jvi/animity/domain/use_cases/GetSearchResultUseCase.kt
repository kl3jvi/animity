package com.kl3jvi.animity.domain.use_cases

import androidx.paging.PagingData
import com.kl3jvi.animity.data.model.AnimeMetaModel
import com.kl3jvi.animity.data.repository.SearchRepositoryImpl
import com.kl3jvi.animity.utils.Constants
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GetSearchResultUseCase @Inject constructor(
    private val searchRepository: SearchRepositoryImpl
) {
    operator fun invoke(searchQuery: String): Flow<PagingData<AnimeMetaModel>>{
        return searchRepository.fetchSearchData(
            Constants.getHeader(),
            searchQuery
        )
    }
}
