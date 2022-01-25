package com.kl3jvi.animity.utils

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow


fun <T> observeLiveData(
    liveData: LiveData<T>,
    owner: LifecycleOwner,
    observer: (T) -> Unit
) {
    liveData.observe(owner) {
        observer(it)
    }
}

fun <T> LifecycleOwner.collectFlow(flow: Flow<T>, collector: suspend (T) -> Unit) {
    lifecycleScope.launchWhenStarted {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.collect {
                collector(it)
            }
        }
    }
}