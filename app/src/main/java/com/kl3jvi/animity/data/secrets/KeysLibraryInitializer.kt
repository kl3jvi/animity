package com.kl3jvi.animity.data.secrets

import android.content.Context
import androidx.startup.Initializer

object LibraryInitializer : Initializer<Unit> {
    fun initialize() {
        System.loadLibrary("keys")
    }

    override fun create(context: Context) {
        initialize()
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}
