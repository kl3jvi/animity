package com.kl3jvi.animity.data.mapper

import com.kl3jvi.animity.FavoritesAnimeQuery
import junit.framework.Assert.assertEquals
import org.junit.Test

class FavoriteMapperQueryTest {
    @Test
    fun favoritesAnimeQuery_can_convert_to_List_of_AniList_Media() {
        val data = FavoritesAnimeQuery.Data(
            user = FavoritesAnimeQuery.User(
                id = 1,
                name = "Klejvi",
                favourites = FavoritesAnimeQuery.Favourites(
                    anime = FavoritesAnimeQuery.Anime(
                        listOf(
                            FavoritesAnimeQuery.Edge(
                                node = FavoritesAnimeQuery.Node(
                                    id = 1,
                                    title = FavoritesAnimeQuery.Title(
                                        userPreferred = "One Piece",
                                        romaji = ""
                                    ),
                                    coverImage = FavoritesAnimeQuery.CoverImage(large = "https://example.com"),
                                    description = "Lorem Ipsum dolor sit amet ...",
                                    nextAiringEpisode = FavoritesAnimeQuery.NextAiringEpisode(
                                        airingAt = 1,
                                        episode = 100
                                    )
                                ),
                                favouriteOrder = 1
                            )
                        ),
                        FavoritesAnimeQuery.PageInfo(
                            total = 1,
                            perPage = 25,
                            currentPage = 2,
                            lastPage = 5,
                            hasNextPage = true
                        )
                    )
                )
            )
        )

        val convertedData = data.convert()

        assertEquals(1, convertedData.first().idAniList)
        assertEquals("One Piece", convertedData.first().title.userPreferred)
        assertEquals("https://example.com", convertedData.first().coverImage.large)
        assertEquals("Lorem Ipsum dolor sit amet ...", convertedData.first().description)
    }
}