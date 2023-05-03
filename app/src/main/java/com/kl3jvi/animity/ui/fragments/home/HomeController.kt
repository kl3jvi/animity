package com.kl3jvi.animity.ui.fragments.home

import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.benasher44.uuid.Uuid
import com.kl3jvi.animity.CardAnimeBindingModel_
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.data.enums.Title
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.title
import com.kl3jvi.animity.utils.navigateSafe
import com.kl3jvi.animity.vertical

fun EpoxyController.buildHome(homeData: HomeData, firebaseAnalytics: Analytics) {
    homeData.run {
        listOf(trendingAnime, popularAnime, movies)
    }.forEachIndexed { index, list ->
        title {
            id(Uuid.randomUUID().toString())
            title(Title.values()[index].title)
        }
        carousel {
            id(Uuid.randomUUID().toString())
            models(list.modelCardAnime(firebaseAnalytics))
        }
    }
    title {
        id(Uuid.randomUUID().toString())
        title(Title.values().last().title)
    }
    homeData.run(HomeData::review).forEach { media ->
        vertical {
            id(Uuid.randomUUID().toString())
            animeInfo(media)
            clickListener { view ->
                HomeFragmentDirections.toReviews(media)
                    .also { view.navigateSafe(it) }
            }
        }
    }
}

fun List<AniListMedia>.modelCardAnime(firebaseAnalytics: Analytics): List<CardAnimeBindingModel_> {
    return map { media ->
        CardAnimeBindingModel_()
            .id(Uuid.randomUUID().toString())
            .clickListener { view ->
                val direction =
                    HomeFragmentDirections.toDetails(media, 0)
                view.navigateSafe(direction)

                firebaseAnalytics.logEvent(
                    media.title.userPreferred.replace("[ ,:](?!_)".toRegex(), ""),
                    mapOf("genre" to media.genres.firstOrNull()?.name.orEmpty())
                )
            }.animeInfo(media)
    }
}
