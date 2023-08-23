package com.kl3jvi.animity.ui.fragments.favorites

import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.kl3jvi.animity.CardAnimeBindingModel_
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.utils.logError

class FavoritesSearchController(private val firebaseAnalytics: Analytics) :
    PagingDataEpoxyController<AniListMedia>() {
    /**
     * It creates a new EpoxyModel for each item in the list.
     *
     * @param currentPosition Int - The current position of the item in the list
     * @param item AniListMedia? - The item that is being bound to the view.
     * @return A EpoxyModel<*>
     */
    override fun buildItemModel(
        currentPosition: Int,
        item: AniListMedia?,
    ): EpoxyModel<*> {
        return CardAnimeBindingModel_().id(item?.idAniList).clickListener { view ->
            try {
                item?.let {
                    val directions =
                        FavoritesFragmentDirections.favoriteToDetails(item, 0)
                    view.findNavController().navigate(directions)
                    val params = mapOf(
                        "genre" to item.genres.firstOrNull()?.name,
                    )
                    firebaseAnalytics.logEvent(
                        item.title.userPreferred.replace("[ ,:](?!_)".toRegex(), ""),
                        params,
                    )
                }
            } catch (e: Exception) {
                logError(e)
            }
        }.animeInfo(item)
    }
}
