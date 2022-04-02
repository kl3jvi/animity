package com.kl3jvi.animity.ui.fragments.search

import androidx.navigation.findNavController
import com.airbnb.epoxy.EpoxyModel
import com.airbnb.epoxy.paging3.PagingDataEpoxyController
import com.kl3jvi.animity.SearchLayoutBindingModel_
import com.kl3jvi.animity.data.model.ui_models.Media
import com.kl3jvi.animity.utils.logError

class PagingSearchController : PagingDataEpoxyController<Media>() {
    override fun buildItemModel(currentPosition: Int, item: Media?): EpoxyModel<*> {
        return SearchLayoutBindingModel_()
            .id(item?.idAniList)
            .clickListener { view ->
                try {
                    item?.let {
                        val directions =
                            SearchFragmentDirections.actionNavigationExploreToNavigationDetails(item)
                        view.findNavController().navigate(directions)
                    }
                } catch (e: Exception) {
                    logError(e)
                }
            }
            .animeInfo(item)
    }
}