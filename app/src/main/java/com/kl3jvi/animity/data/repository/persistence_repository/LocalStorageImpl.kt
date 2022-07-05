package com.kl3jvi.animity.data.repository.persistence_repository

import com.google.gson.Gson


inline fun <reified T> String?.fromJson(): T {
    return Gson().fromJson(this.orEmpty(), T::class.java)
}

inline fun <reified T> T?.toJson(): String? {
    return Gson().toJson(this)
}

