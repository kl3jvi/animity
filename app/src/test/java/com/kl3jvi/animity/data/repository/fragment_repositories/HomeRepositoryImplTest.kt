@file:OptIn(ApolloExperimental::class)

package com.kl3jvi.animity.data.repository.fragment_repositories

import com.apollographql.apollo3.ApolloClient
import com.apollographql.apollo3.annotations.ApolloExperimental
import com.apollographql.apollo3.exception.ApolloNetworkException
import com.apollographql.apollo3.testing.QueueTestNetworkTransport
import com.apollographql.apollo3.testing.enqueueTestResponse
import com.kl3jvi.animity.HomeDataQuery
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeApiClient
import com.kl3jvi.animity.data.network.anime_service.GogoAnimeService
import com.kl3jvi.animity.domain.repositories.fragment_repositories.HomeRepository
import com.kl3jvi.animity.util.*
import com.kl3jvi.animity.utils.parser.Parser
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.kotlin.mock

@ExperimentalCoroutinesApi
class HomeRepositoryImplTest {
    private lateinit var repository: HomeRepository
    private lateinit var client: GogoAnimeApiClient
    private lateinit var apolloClient: ApolloClient

    private val parser: Parser = mock()
    private val service: GogoAnimeService = mock()


    @get:Rule
    val coroutinesRule = MainCoroutinesRule()

    @Before
    fun setup() {
        apolloClient = ApolloClient.Builder()
            .networkTransport(QueueTestNetworkTransport())
            .build()
        client = GogoAnimeApiClient(service, apolloClient)
        repository = HomeRepositoryImpl(client, coroutinesRule.testDispatcher, parser)
    }

    @Test
    fun getHomeDataSuccessful() = runTest {
        val mockData = HomeDataQuery.Data(
            trendingAnime = trendingAnimeModel,
            movies = moviesModel,
            popularAnime = popularAnimeModel,
            review = reviewModel
        )
        val testQuery = HomeDataQuery()
        apolloClient.enqueueTestResponse(testQuery, mockData)
        val actual = apolloClient.query(testQuery).execute().data!!
        assertEquals(
            mockData.movies?.media?.first()?.homeMedia?.id,
            actual.movies?.media?.first()?.homeMedia?.id
        )
    }

    @Test
    fun getHomeDataCatchError() = runTest {

        val testQuery = HomeDataQuery()
//        apolloClient.enqueueTestResponse(testQuery, )
        val data = apolloClient.query(testQuery).execute()
        assertEquals(data, ApolloNetworkException())
//        assertEquals(data?.movies?.media?.first()?.homeMedia?.id, Unit)
    }
}