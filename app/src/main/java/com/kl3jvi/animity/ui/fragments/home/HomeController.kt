package com.kl3jvi.animity.ui.fragments.home

import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.benasher44.uuid.Uuid
import com.kl3jvi.animity.CardAnimeBindingModel_
import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.data.model.ui_models.Media
import com.kl3jvi.animity.title
import com.kl3jvi.animity.utils.navigateSafe
import com.kl3jvi.animity.vertical


fun EpoxyController.buildHome(homeData: HomeData) {

    val (trendingAnime, newAnime, popularAnime, reviews) = homeData

    listOf(
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
        vertical {
            id(Uuid.randomUUID().toString())
            animeInfo(media)
            clickListener { view ->
                val directions =
                    HomeFragmentDirections.actionNavigationHomeToDetailsFragment(media)
                view.navigateSafe(directions)
            }
        }
    }
}


enum class Title(val title: String) {
    TRENDING_ANIME(title = "Trending Anime"),
    NEW_ANIME(title = "New Anime"),
    MOVIES(title = "Movies"),
    REVIEWS(title = "Reviews")
}

fun List<Media>.modelCardAnime(): List<CardAnimeBindingModel_> {
    return map { media ->
        CardAnimeBindingModel_()
            .id(Uuid.randomUUID().toString())
            .clickListener { view ->
                val direction =
                    HomeFragmentDirections.actionNavigationHomeToDetailsFragment(media)
                view.navigateSafe(direction)
            }
            .animeInfo(media)

    }
}





