package com.kl3jvi.animity.ui.fragments.home

import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyController
import com.airbnb.epoxy.carousel
import com.kl3jvi.animity.CardAnimeBindingModel_
import com.kl3jvi.animity.data.model.ui_models.AnimeMetaModel
import com.kl3jvi.animity.title
import com.kl3jvi.animity.vertical


fun EpoxyController.buildHome(listOfAnimes: MutableList<List<AnimeMetaModel>>) {
    title {
        id(1)
        title("Recent Sub")
    }
    carousel {
        id(2)
        models(listOfAnimes[0].modelCardAnime())
    }
    title {
        id(3)
        title("New Season")
    }
    carousel {
        id(4)
        models(listOfAnimes[1].modelCardAnime())
    }
    title {
        id(5)
        title("Movies")
    }
    carousel {
        id(6)
        models(listOfAnimes[2].modelCardAnime())
    }
    title {
        id(7)
        title("Popular")
    }
    listOfAnimes[3].forEach {
        vertical {
            id(it.id)
            clickListener { view ->
                val direction =
                    HomeFragmentDirections.actionNavigationHomeToDetailsFragment(
                        it
                    )
                view.findNavController().navigate(direction)
            }
            animeInfo(it)
        }
    }


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
