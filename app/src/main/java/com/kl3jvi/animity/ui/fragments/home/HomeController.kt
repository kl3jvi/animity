package com.kl3jvi.animity.ui.fragments.home

import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.kl3jvi.animity.CardAnimeBindingModel_
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.title


fun EpoxyController.buildHome(listOfAnimes: MutableList<List<AnimeMetaModel>>) {

    listOfAnimes.forEachIndexed { index, list ->
        title {
            id(index)
            title(Title.values()[index].title)
        }
        carousel {
            id(index.times(32).hashCode().times(2))
            models(list.modelCardAnime())
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
            }.animeInfo(animeMetaModel)
    }
}
