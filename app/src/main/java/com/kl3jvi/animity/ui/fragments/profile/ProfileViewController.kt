package com.kl3jvi.animity.ui.fragments.profile

import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.*
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.utils.Constants
import com.kl3jvi.animity.utils.Constants.Companion.randomId


fun EpoxyController.buildProfile(
    userData: ApolloResponse<UserQuery.Data>?,
    animeCollectionResponse: ApolloResponse<AnimeListCollectionQuery.Data>?
) {

    profileCard {
        id(randomId())
        bgImage(Constants.DEFAULT_COVER)
        userData(userData?.data)
    }
    val listOfData = animeCollectionResponse?.data?.media?.lists
    if (!listOfData.isNullOrEmpty()) {
        listOfData.map {
            title {
                id(randomId())
                title(it?.name)
            }
            carousel {
                id(randomId())
                models(
                    it?.entries?.mapToAnimeMetaModel()?.modelCardAnimeProfile()
                        ?: emptyList()
                )
            }
        }
    } else
        noAnime {
            id(randomId())
        }

}

fun List<AnimeMetaModel>.modelCardAnimeProfile(): List<CardAnimeBindingModel_> {
    return map { animeMetaModel ->
        CardAnimeBindingModel_()
            .id(animeMetaModel.id)
            .clickListener { view ->
//                val direction =
//                    ProfileFragmentDirections.actionNavigationProfileToNavigationDetails(
//                        animeMetaModel
//                    )
//                view.findNavController().navigate(direction)
            }
    }
}


private fun List<AnimeListCollectionQuery.Entry?>.mapToAnimeMetaModel(): List<AnimeMetaModel> {
    return map { animeWatchedData ->
        AnimeMetaModel(
            id = animeWatchedData?.media?.id ?: 0,
            title = animeWatchedData?.media?.title?.romaji.toString(),
            imageUrl = animeWatchedData?.media?.coverImage?.large.toString(),
            categoryUrl = null
        )
    }
}

