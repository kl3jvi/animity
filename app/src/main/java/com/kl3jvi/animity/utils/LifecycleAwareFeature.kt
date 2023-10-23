package com.kl3jvi.animity.utils

import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner

/**
 * An interface for all entry points to feature components to implement in order to make them lifecycle aware.
 */
interface LifecycleAwareFeature : DefaultLifecycleObserver {
    /**
     * Method that is called after ON_START event occurred.
     */
    fun start()

    /**
     * Method that is called after ON_STOP event occurred.
     */
    fun stop()

    override fun onStart(owner: LifecycleOwner) {
        start()
    }

    override fun onStop(owner: LifecycleOwner) {
        stop()
    }
}
