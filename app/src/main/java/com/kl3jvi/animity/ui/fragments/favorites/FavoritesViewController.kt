package com.kl3jvi.animity.ui.fragments.favorites

import android.util.Log
import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyController
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.favoriteAnime

fun EpoxyController.buildFavorites(data: List<AniListMedia>) {
    data.forEach { media ->
        favoriteAnime {
            id(media.idAniList)
            clickListener { view ->
                Log.e("Media", media.toString())
                val directions =
                    FavoritesFragmentDirections.actionNavigationFavoritesToNavigationDetails(
                        media,
                        0
                    )
                view.findNavController().navigate(directions)
            }
            animeInfo(media)
        }
    }
}
