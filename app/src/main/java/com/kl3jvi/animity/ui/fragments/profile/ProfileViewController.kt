package com.kl3jvi.animity.ui.fragments.profile

import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.benasher44.uuid.Uuid
import com.kl3jvi.animity.*
import com.kl3jvi.animity.data.mapper.ProfileData
import com.kl3jvi.animity.data.mapper.ProfileRow
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.data.model.ui_models.Media
import com.kl3jvi.animity.ui.fragments.home.modelCardAnime
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Constants.Companion.randomId
import com.kl3jvi.animity.utils.logMessage


fun EpoxyController.buildProfile(
    userData: ProfileData?,
    animeCollectionResponse: List<ProfileRow>
) {
    logMessage(animeCollectionResponse.toString())
    profileCard {
        id(randomId())
        userData?.userData?.let {
            bgImage(it.bannerImage.ifEmpty { Constants.DEFAULT_COVER })
            userData(it)
        }
    }
    if (!animeCollectionResponse.isNullOrEmpty()) {
        animeCollectionResponse.map { profileRow->
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
                view.findNavController().navigate(direction)
            }.animeInfo(media)
    }
}

