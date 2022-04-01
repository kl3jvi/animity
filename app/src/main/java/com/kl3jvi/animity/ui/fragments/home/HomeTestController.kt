package com.kl3jvi.animity.ui.fragments.home

import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.benasher44.uuid.Uuid
import com.kl3jvi.animity.CardAnimeBindingModel_
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.data.model.ui_models.Media
import com.kl3jvi.animity.title
import com.kl3jvi.animity.utils.logMessage
import com.kl3jvi.animity.vertical

fun EpoxyController.buildHomeTest(homeData: HomeData) {

    val (trendingAnime, newAnime, popularAnime, reviews) = homeData

    val t = listOf(
        homeData.newAnime,
        homeData.trendingAnime,
        homeData.movies
    ).forEachIndexed { index, list ->
        title {
            id(Uuid.randomUUID().toString())
            title(Title.values()[index].title)
        }
        carousel {
            id(Uuid.randomUUID().toString())
            models(list.modelCardAnime())
        }
    }

    title {
        id(Uuid.randomUUID().toString())
        title(Title.values()[3].title)
    }
    popularAnime.forEach { media ->
        logMessage(media.averageScore.toString())
        vertical {
            id(Uuid.randomUUID().toString())
            animeInfo(media)
        }
    }

}


fun List<Media>.modelCardAnime(): List<CardAnimeBindingModel_> {
    return map { media ->
        CardAnimeBindingModel_()
            .id(Uuid.randomUUID().toString())
            .clickListener { view ->
                val direction =
                    HomeFragmentDirections.actionNavigationHomeToDetailsFragment(
                        AnimeMetaModel(
                            id = media.idAniList,
                            typeValue = 1,
                            imageUrl = media.coverImage.large,
                            title = media.title.userPreferred,
                            categoryUrl = null
                        )
                    )
                view.findNavController().navigate(direction)
            }
            .animeInfo(media)

    }
}