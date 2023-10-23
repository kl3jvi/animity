package com.kl3jvi.animity.ui.fragments.profile.my

import android.view.View
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.kl3jvi.animity.CardAnimeBindingModel_
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.ProfileData
import com.kl3jvi.animity.noAnime
import com.kl3jvi.animity.profileCard
import com.kl3jvi.animity.title
import com.kl3jvi.animity.ui.fragments.profile.their.TheirProfileDirections
import com.kl3jvi.animity.utils.navigateSafe

const val DEFAULT_COVER = "https://bit.ly/3p6DE28"

fun EpoxyController.buildProfile(
    profileType: ProfileType,
    userData: ProfileData?,
    listener: (View) -> Unit = {},
) {
    profileCard {
        id(userData?.userData?.id)
        userData?.userData?.let {
            backgroundImage(it.bannerImage.ifEmpty(::DEFAULT_COVER))
            userData(it)
            state(userData.followState.first)
            textState(userData.followState.second)
            followButtonShow(profileType)
            clickListener(listener)
        }
    }
    userData?.profileRow?.forEach { profileRow ->
        title {
            id(profileRow.title)
            title(profileRow.title)
        }
        carousel {
            id("carousel_${profileRow.title}")
            models(profileRow.anime.modelCardAnimeProfile(profileType))
        }
    } ?: noAnime { id("no_anime") }
}

// A function that takes a list of AniListMedia and returns a list of CardAnimeBindingModel_
fun List<AniListMedia>.modelCardAnimeProfile(profileType: ProfileType): List<CardAnimeBindingModel_> {
    return map { media ->
        CardAnimeBindingModel_()
            .id(media.idAniList)
            .clickListener { view ->
                val direction =
                    when (profileType) {
                        ProfileType.ME -> ProfileFragmentDirections.profileToDetails(media, 0)
                        ProfileType.OTHER -> TheirProfileDirections.theirProfileToDetails(media, 0)
                    }
                view.navigateSafe(direction)
            }.animeInfo(media)
    }
}

enum class ProfileType {
    ME,
    OTHER,
}
