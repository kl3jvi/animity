package com.kl3jvi.animity.ui.fragments.home

import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.kl3jvi.animity.CardAnimeBindingModel_
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.data.enums.Title
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.title
import com.kl3jvi.animity.utils.navigateSafe
import com.kl3jvi.animity.vertical

fun EpoxyController.buildHome(
    homeData: HomeData,
    firebaseAnalytics: Analytics,
) {
    homeData.run {
        listOf(trendingAnime, popularAnime, movies)
    }.forEachIndexed { index, list ->
        title {
            id("title_${Title.entries[index].title}")
            title(Title.entries[index].title)
        }
        carousel {
            id("carousel_${Title.entries[index].title}")
            models(list.modelCardAnime(firebaseAnalytics))
        }
    }
    title {
        id("title_${Title.entries.last().title}")
        title(Title.entries.last().title)
    }
    homeData.run(HomeData::review).forEach { media ->
        vertical {
            id(media.mediaId)
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
            .id(media.idAniList)
            .clickListener { view ->
                val direction =
                    HomeFragmentDirections.toDetails(media, 0)
                view.navigateSafe(direction)
                val animeName = media.title.userPreferred.replace("[ ,:](?!_)".toRegex(), "")
                firebaseAnalytics.logEvent("anime_viewed", mapOf("anime_title" to animeName))
            }.animeInfo(media)
    }
}
