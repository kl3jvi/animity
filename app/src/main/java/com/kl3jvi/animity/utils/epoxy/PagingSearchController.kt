package com.kl3jvi.animity.utils.epoxy

import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.kl3jvi.animity.SearchLayoutBindingModel_
import com.kl3jvi.animity.SearchSuggestionBindingModel_
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.data.enums.SearchMode
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.User
import com.kl3jvi.animity.ui.fragments.search.SearchFragmentDirections
import com.kl3jvi.animity.utils.logError

class PagingSearchController<T : Any>(
    private val firebaseAnalytics: Analytics,
    private val searchMode: SearchMode,
    private val myId: Int,
) : PagingDataEpoxyController<T>() {
    /**
     * It creates a new EpoxyModel for each item in the list.
     *
     * @param currentPosition Int - The current position of the item in the list
     * @param item AniListMedia? - The item that is being bound to the view.
     * @return A EpoxyModel<*>
     */
    override fun buildItemModel(currentPosition: Int, item: T?): EpoxyModel<*> {
        return when (searchMode) {
            SearchMode.ANIME -> createAnimeModel(item as? AniListMedia)
            SearchMode.USERS -> createUserModel(item as? User, myId)
        }
    }

    private fun createAnimeModel(item: AniListMedia?): EpoxyModel<*> {
        return SearchLayoutBindingModel_()
            .id(item?.idAniList)
            .clickListener { view ->
                try {
                    item?.let {
                        val directions =
                            SearchFragmentDirections.exploreToDetails(item, 0)
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

    private fun createUserModel(item: User?, myId: Int): EpoxyModel<*> {
        return SearchSuggestionBindingModel_()
            .id(item?.id)
            .clickListener { view ->
                try {
                    item?.let {
                        val directions =
                            if (item.id == myId) {
                                SearchFragmentDirections.toMyProfile(item)
                            } else {
                                SearchFragmentDirections.toTheirProfile(item)
                            }
                        view.findNavController().navigate(directions)
                    }
                } catch (e: Exception) {
                    logError(e)
                }
            }
            .user(item)
    }
}
