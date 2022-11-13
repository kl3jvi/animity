package com.kl3jvi.animity.ui.fragments.profile

import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.kl3jvi.animity.CardAnimeBindingModel_
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.ProfileData
import com.kl3jvi.animity.noAnime
import com.kl3jvi.animity.profileCard
import com.kl3jvi.animity.title
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Constants.Companion.randomId
import com.kl3jvi.animity.utils.navigateSafe

fun EpoxyController.buildProfile(
    userData: ProfileData?
) {
    profileCard {
        id(randomId())
        userData?.userData?.let {
            backgroundImage(it.bannerImage.ifEmpty { Constants.DEFAULT_COVER })
            userData(it)
        }
    }
    if (userData?.profileRow?.isNotEmpty() == true) {
        userData.profileRow.map { profileRow ->
            title {
                id(randomId())
                title(profileRow.title)
            }
            carousel {
                id(randomId())
                models(profileRow.anime.modelCardAnimeProfile())
            }
        }
    } else {
        noAnime {
            id(randomId())
        }
    }
}

/* A function that takes a list of AniListMedia and returns a list of CardAnimeBindingModel_ */
fun List<AniListMedia>.modelCardAnimeProfile(): List<CardAnimeBindingModel_> {
    return map { media ->
        CardAnimeBindingModel_()
            .id(randomId())
            .clickListener { view ->
                val direction =
                    ProfileFragmentDirections.actionNavigationProfileToNavigationDetails(
                        media
                    )
                view.navigateSafe(direction)
            }.animeInfo(media)
    }
}
