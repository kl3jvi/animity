package com.kl3jvi.animity.data.mapper

import com.kl3jvi.animity.FavoritesAnimeQuery
import com.kl3jvi.animity.util.userFavoritesModel
import org.junit.Test

class FavoriteMapperQueryTest {
    @Test
    fun favoritesAnimeQuery_can_convert_to_List_of_AniList_Media() {
        val data = FavoritesAnimeQuery.Data(
            user = userFavoritesModel
        )


//
//
//        assertEquals(1, convertedData.data.first().idAniList)
//        assertEquals("One Piece", convertedData.first().title.userPreferred)
//        assertEquals("https://example.com", convertedData.first().coverImage.large)
//        assertEquals("Lorem Ipsum dolor sit amet ...", convertedData.first().description)
    }
}