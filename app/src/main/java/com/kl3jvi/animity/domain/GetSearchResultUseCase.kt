package com.kl3jvi.animity.domain

import com.kl3jvi.animity.model.entities.AnimeMetaModel
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Resource
import com.kl3jvi.animity.view.fragments.search.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class GetSearchResultUseCase @Inject constructor(private val searchRepository: SearchRepository) {


    fun getSearchData(searchQuery: String): Flow<Resource<List<AnimeMetaModel>>> = flow {
        try {
            emit(Resource.Loading())
            val response =
                Constants.parseList(
                    searchRepository.fetchSearchData(
                        Constants.getHeader(),
                        searchQuery,
                        1
                    ).string(), Constants.TYPE_MOVIE
                ).toList()
            emit(
                Resource.Success(
                    data = response
                )
            )
        } catch (e: HttpException) {
            emit(
                Resource.Error(
                    message = e.localizedMessage ?: "An unexpected error occurred",
                    Constants.parseList(
                        searchRepository.fetchSearchData(
                            Constants.getHeader(),
                            searchQuery,
                            1
                        ).string(), Constants.TYPE_MOVIE
                    ).toList()
                )
            )
        } catch (e: IOException) {
            emit(
                Resource.Error(
                    e.localizedMessage ?: "Couldn't reach server. Check your internet connection.",
                    Constants.parseList(
                        searchRepository.fetchSearchData(
                            Constants.getHeader(),
                            searchQuery,
                            1
                        ).string(), Constants.TYPE_MOVIE
                    ).toList()
                )
            )
        }
    }

}