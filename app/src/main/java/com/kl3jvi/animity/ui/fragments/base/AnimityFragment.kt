package com.kl3jvi.animity.ui.fragments.base

import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.kl3jvi.animity.ui.activities.main.MainViewModel
import com.kl3jvi.animity.ui.fragments.StateManager
import com.kl3jvi.animity.ui.fragments.StateManagerFactory
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow

abstract class AnimityFragment(@LayoutRes contentLayoutId: Int) : Fragment(contentLayoutId),
    StateManagerFactory {

    private val stateManager by lazy { create() }
    private val mainViewModel by activityViewModels<MainViewModel>()

    abstract fun showLoading(show: Boolean)
    abstract fun handleError(e: Throwable)
    abstract fun handleNetworkChanges(isConnected: Boolean)

    override fun create(): StateManager {
        return object : StateManager() {
            override fun showLoadingState(show: Boolean) = showLoading(show)
            override fun handleErrorState(e: Throwable) = handleError(e)
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun onStart() {
        super.onStart()
        stateManager.handleNetworkChanges(
            emptyFlow(),
            lifecycle,
            ::handleNetworkChanges
        )
    }


    override fun onResume() {
        super.onResume()
        stateManager.handleNetworkChanges(
            emptyFlow(),
            lifecycle,
            ::handleNetworkChanges
        )
    }
}