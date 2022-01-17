package com.kl3jvi.animity.domain.use_cases

import androidx.paging.PagingData
import com.kl3jvi.animity.data.model.AnimeMetaModel
import com.kl3jvi.animity.data.repository.SearchRepositoryImpl
import com.kl3jvi.animity.domain.repositories.SearchRepository
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.toList
import retrofit2.HttpException
import java.io.IOException
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
