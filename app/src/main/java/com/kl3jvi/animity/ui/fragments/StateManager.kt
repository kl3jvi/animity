package com.kl3jvi.animity.ui.fragments

interface StateManager {
    fun showLoading(show: Boolean)

    fun handleError(e: Throwable)
}
