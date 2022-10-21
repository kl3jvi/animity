package com.kl3jvi.animity.data.mapper

import com.kl3jvi.animity.HomeDataQuery
import com.kl3jvi.animity.util.moviesModel
import com.kl3jvi.animity.util.popularAnimeModel
import com.kl3jvi.animity.util.reviewModel
import com.kl3jvi.animity.util.trendingAnimeModel
import org.junit.Test

class HomeDataQueryConverter {
    @Test
    fun homeData_query_can_be_converted_to_Home_Media() {
        val data = HomeDataQuery.Data(
            trendingAnime = trendingAnimeModel,
            movies = moviesModel,
            popularAnime = popularAnimeModel,
            review = reviewModel
        )

        val convertedData = data

        // Test for trending media
//        assertEquals("One Piece", convertedData.trendingAnime.first().title.userPreferred)
//
//        // Test for popular media
//        assertEquals("AOT", convertedData.popularAnime.first().title.userPreferred)
//
//        // Test for movies media
//        assertEquals("Naruto", convertedData.movies.first().title.userPreferred)
//
//        // test for reviews
//        assertEquals("Lorem Ipsum", convertedData.review.first().summary)
    }
}
