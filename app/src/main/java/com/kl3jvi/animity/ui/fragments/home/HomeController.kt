package com.kl3jvi.animity.ui.fragments.home

import android.os.Bundle
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.benasher44.uuid.Uuid
import com.google.firebase.analytics.FirebaseAnalytics
import com.kl3jvi.animity.CardAnimeBindingModel_
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.HomeData
import com.kl3jvi.animity.title
import com.kl3jvi.animity.utils.navigateSafe
import com.kl3jvi.animity.vertical


fun EpoxyController.buildHome(homeData: HomeData, firebaseAnalytics: FirebaseAnalytics) {
    val (trendingAnime, newAnime, movies, reviews) = homeData
    listOf(
        trendingAnime,
        newAnime,
        movies
    ).forEachIndexed { index, list ->
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
        title(Title.values()[3].title)
    }
    reviews.forEach { media ->
        vertical {
            id(Uuid.randomUUID().toString())
            animeInfo(media)
            clickListener { view ->
                val directions =
                    HomeFragmentDirections.actionNavigationHomeToReviewDetailsFragment(media)
                view.navigateSafe(directions)
            }
        }
    }
}


enum class Title(val title: String) {
    TRENDING_ANIME(title = "Trending"),
    NEW_ANIME(title = "Popular"),
    MOVIES(title = "Movies"),
    REVIEWS(title = "Reviews")
}

fun List<AniListMedia>.modelCardAnime(firebaseAnalytics: FirebaseAnalytics): List<CardAnimeBindingModel_> {
    /* It's a function that takes a list of AniListMedia and returns a list of CardAnimeBindingModel_ */
    return map { media ->
        CardAnimeBindingModel_()
            .id(Uuid.randomUUID().toString())
            .clickListener { view ->
                val direction =
                    HomeFragmentDirections.actionNavigationHomeToDetailsFragment(media)
                view.navigateSafe(direction)
                val params = Bundle()
                params.putString(
                    "genre",
                    media.genres.firstOrNull()?.name ?: "empty"
                )
                firebaseAnalytics.logEvent(
                    media.title.userPreferred.replace("\\s".toRegex(), ""),
                    params
                )
            }
            .animeInfo(media)
    }
}





