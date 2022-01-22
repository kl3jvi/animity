package com.kl3jvi.animity.utils

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData


fun <T> observeLiveData(
    liveData: LiveData<T>,
    owner: LifecycleOwner,
    observer: (T) -> Unit
) {
    liveData.observe(owner) {
        observer(it)
    }
}

fun getSafeString(string: String?) = string.toString()