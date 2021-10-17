package com.kl3jvi.animity.view.factory

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.kl3jvi.animity.model.network.ApiHelper
import com.kl3jvi.animity.view.activities.player.PlayerRepository
import com.kl3jvi.animity.view.activities.player.PlayerViewModel
import com.kl3jvi.animity.view.fragments.details.DetailsRepository
import com.kl3jvi.animity.view.fragments.details.DetailsViewModel
import com.kl3jvi.animity.view.fragments.home.HomeRepository
import com.kl3jvi.animity.view.fragments.home.HomeViewModel

@Suppress("UNCHECKED_CAST")
class ViewModelFactory(private val apiHelper: ApiHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(PlayerViewModel::class.java) -> PlayerViewModel(
                PlayerRepository(apiHelper)
            ) as T
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> HomeViewModel(
                HomeRepository(apiHelper)
            ) as T
            modelClass.isAssignableFrom(DetailsViewModel::class.java) -> DetailsViewModel(
                DetailsRepository(apiHelper)
            ) as T
            else -> throw IllegalArgumentException("Unknown class name")
        }
    }
}