package com.kl3jvi.animity.utils

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


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

fun <T> Flow<NetworkResource<T>>.mapToState(): Flow<State<T>> = map { resource ->
    State.fromResource(resource)
}