package com.kl3jvi.animity.data.mapper

import com.kl3jvi.animity.HomeDataQuery
import com.kl3jvi.animity.fragment.HomeMedia
import com.kl3jvi.animity.type.MediaFormat
import com.kl3jvi.animity.type.MediaStatus
import com.kl3jvi.animity.type.MediaType
import junit.framework.Assert.assertEquals
import org.junit.Test

class HomeDataQueryConverter {
    @Test
    fun homeData_query_can_be_converted_to_Home_Media() {
        val data = HomeDataQuery.Data(
            trendingAnime = HomeDataQuery.TrendingAnime(
                media = listOf(
                    HomeDataQuery.Medium(
                        __typename = "Trending Media",
                        homeMedia = HomeMedia(
                            id = 1,
                            idMal = 2,
                            title = HomeMedia.Title(
                                romaji = "One Piece",
                                userPreferred = "One Piece"
                            ),
                            type = MediaType.ANIME,
                            status = MediaStatus.HIATUS,
                            format = MediaFormat.MANGA,
                            description = "Lorem Ipsum dolor sit amet ...",
                            coverImage = HomeMedia.CoverImage(
                                large = "https://github.com",
                                medium = "https://example.com",
                                extraLarge = "https://example.com"
                            ),
                            streamingEpisodes = listOf<HomeMedia.StreamingEpisode>(

                            ),
                            nextAiringEpisode = HomeMedia.NextAiringEpisode(
                                airingAt = 1,
                                episode = 2,
                            ),
                            "https://github.com",
                            genres = listOf<String>("Action", "Fantasy"),
                            averageScore = 1,
                            favourites = 30,
                            startDate = HomeMedia.StartDate(2000, 1, 3)
                        )
                    )
                )
            ),
            movies = HomeDataQuery.Movies(
                media = listOf(
                    HomeDataQuery.Medium2(
                        __typename = "Movies",
                        homeMedia = HomeMedia(
                            id = 1,
                            idMal = 2,
                            title = HomeMedia.Title(
                                romaji = "Naruto",
                                userPreferred = "Naruto"
                            ),
                            type = MediaType.ANIME,
                            status = MediaStatus.HIATUS,
                            format = MediaFormat.MANGA,
                            description = "Lorem Ipsum dolor sit amet ...",
                            coverImage = HomeMedia.CoverImage(
                                large = "https://naruto.com",
                                medium = "https://naruto.com",
                                extraLarge = "https://naruto.com"
                            ),
                            streamingEpisodes = listOf<HomeMedia.StreamingEpisode>(

                            ),
                            nextAiringEpisode = HomeMedia.NextAiringEpisode(
                                airingAt = 1,
                                episode = 2,
                            ),
                            "https://naruto.com",
                            genres = listOf<String>("Fantasy", "Animation"),
                            averageScore = 1,
                            favourites = 30,
                            startDate = HomeMedia.StartDate(1999, 3, 5)
                        )
                    )
                )
            ),
            popularAnime = HomeDataQuery.PopularAnime(
                media = listOf(
                    HomeDataQuery.Medium1(
                        __typename = "Popular Anime",
                        homeMedia = HomeMedia(
                            id = 1,
                            idMal = 2,
                            title = HomeMedia.Title(
                                romaji = "AOT",
                                userPreferred = "AOT"
                            ),
                            type = MediaType.ANIME,
                            status = MediaStatus.HIATUS,
                            format = MediaFormat.MANGA,
                            description = "Lorem Ipsum dolor sit amet ...",
                            coverImage = HomeMedia.CoverImage(
                                large = "https://AOT.com",
                                medium = "https://AOT.com",
                                extraLarge = "https://AOT.com"
                            ),
                            streamingEpisodes = listOf<HomeMedia.StreamingEpisode>(

                            ),
                            nextAiringEpisode = HomeMedia.NextAiringEpisode(
                                airingAt = 1,
                                episode = 2,
                            ),
                            "https://AOT.com",
                            genres = listOf<String>("ATTACK", "FIGHT"),
                            averageScore = 1,
                            favourites = 30,
                            startDate = HomeMedia.StartDate(1873, 12, 1)
                        )
                    )

                )
            ),
            review = HomeDataQuery.Review(
                listOf(
                    HomeDataQuery.Review1(
                        id = 1,
                        userId = 2,
                        mediaId = 3,
                        mediaType = MediaType.ANIME,
                        summary = "Lorem Ipsum",
                        body = "This is a random string",
                        rating = 3,
                        ratingAmount = 19,
                        score = 199,
                        user = HomeDataQuery.User(
                            id = 1,
                            name = "Klejvi",
                            avatar = HomeDataQuery.Avatar(
                                large = "https://avatar1.com/large",
                                medium = "https://avatar1.com/medium"
                            )
                        ),
                        media = HomeDataQuery.Media(
                            1,
                            title = HomeDataQuery.Title(
                                romaji = "Review For XXX",
                                userPreferred = "Review For XXX"
                            ),
                            bannerImage = "https://bannerImage.com",
                            coverImage = HomeDataQuery.CoverImage(large = "https://coverImage.com")

                        )
                    )
                )
            ),
        )

        val convertedData = data.convert()

        // Test for trending media
        assertEquals("One Piece", convertedData.trendingAnime.first().title.userPreferred)

        // Test for popular media
        assertEquals("AOT", convertedData.popularAnime.first().title.userPreferred)

        // Test for movies media
        assertEquals("Naruto", convertedData.movies.first().title.userPreferred)

        // test for reviews
        assertEquals("Lorem Ipsum", convertedData.review.first().summary)

    }
}