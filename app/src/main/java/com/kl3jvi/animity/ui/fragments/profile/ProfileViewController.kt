package com.kl3jvi.animity.ui.fragments.profile

import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.kl3jvi.animity.CardAnimeBindingModel_
import com.kl3jvi.animity.data.mapper.ProfileData
import com.kl3jvi.animity.data.mapper.ProfileRow
import com.kl3jvi.animity.data.model.ui_models.Media
import com.kl3jvi.animity.noAnime
import com.kl3jvi.animity.profileCard
import com.kl3jvi.animity.title
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Constants.Companion.randomId
import com.kl3jvi.animity.utils.logMessage
import com.kl3jvi.animity.utils.navigateSafe


fun EpoxyController.buildProfile(
    userData: ProfileData?,
    animeCollectionResponse: List<ProfileRow>
) {
    logMessage(animeCollectionResponse.toString())
    profileCard {
        id(randomId())
        userData?.userData?.let {
            backgroundImage(it.bannerImage.ifEmpty { Constants.DEFAULT_COVER })
            userData(it)
        }
    }
    if (!animeCollectionResponse.isNullOrEmpty()) {
        animeCollectionResponse.map { profileRow ->
            title {
                id(randomId())
                title(profileRow.title)
            }
            carousel {
                id(randomId())
                models(profileRow.animes.modelCardAnimeProfile())
            }
        }
    } else
        noAnime {
            id(randomId())
        }

}

fun List<Media>.modelCardAnimeProfile(): List<CardAnimeBindingModel_> {
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

