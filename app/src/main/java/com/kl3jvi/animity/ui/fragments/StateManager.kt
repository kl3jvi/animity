package com.kl3jvi.animity.ui.fragments

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.coroutineScope
import androidx.lifecycle.flowWithLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach

abstract class StateManager {
    abstract fun showLoadingState(show: Boolean)

    abstract fun handleErrorState(e: Throwable)

    fun handleNetworkChanges(
        networkConnection: Flow<Boolean>,
        lifecycle: Lifecycle,
        callback: (isConnected: Boolean) -> Unit,
    ) {
        networkConnection.flowWithLifecycle(lifecycle,Lifecycle.State.RESUMED)
            .onEach { isConnected ->
                callback(isConnected)
            }.launchIn(lifecycle.coroutineScope)
    }
}

interface StateManagerFactory {
    fun create(): StateManager
}
