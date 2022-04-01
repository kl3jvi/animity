package com.kl3jvi.animity.ui.fragments.home

import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.benasher44.uuid.Uuid
import com.kl3jvi.animity.CardAnimeBindingModel_
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.title
import com.kl3jvi.animity.vertical


fun EpoxyController.buildHome(listOfAnimes: MutableList<List<AnimeMetaModel>>) {

    val lastIndex = listOfAnimes.lastIndex
    listOfAnimes.forEachIndexed { index, list ->
        title {
            id(Uuid.randomUUID().toString())
            title(Title.values()[index].title)
        }
        if (index != lastIndex) {
            carousel {
                id(Uuid.randomUUID().toString())
                models(list.modelCardAnime())
            }
        } else {
            list.forEach { animeMetaModel ->
                vertical {
                    id(animeMetaModel.id)
                    clickListener { view ->
                        val direction =
                            HomeFragmentDirections.actionNavigationHomeToDetailsFragment(
                                animeMetaModel
                            )
                        view.findNavController().navigate(direction)
                    }
//                    animeInfo(animeMetaModel)
                }
            }
        }

    }
}

enum class Title(val title: String) {
    RECENT_SUB(title = "Recent Sub"),
    NEW_SEASON(title = "New Season"),
    MOVIES(title = "Movies"),
    POPULAR(title = "Popular")
}

fun List<AnimeMetaModel>.modelCardAnime(): List<CardAnimeBindingModel_> {
    return map { animeMetaModel ->
        CardAnimeBindingModel_()
            .id(animeMetaModel.id)
            .clickListener { view ->
                val direction =
                    HomeFragmentDirections.actionNavigationHomeToDetailsFragment(animeMetaModel)
                view.findNavController().navigate(direction)
            }
    }
}





