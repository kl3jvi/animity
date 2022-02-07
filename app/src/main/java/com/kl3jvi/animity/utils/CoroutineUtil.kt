package com.kl3jvi.animity.utils

import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


fun <T> observeLiveData(
    liveData: LiveData<T>,
    owner: LifecycleOwner,
    observer: (T) -> Unit
) {
    liveData.runCatching {
        observe(owner) {
            observer(it)
        }
    }.onFailure {
        it.printStackTrace()
    }
}

fun <T> LifecycleOwner.collectPagingLiveData(
    liveData: LiveData<T>,
    observer: suspend (T) -> Unit
) {
    liveData.observe(this) {
        lifecycleScope.launch {
            observer(it)
        }
    }
}

inline fun <T> LifecycleOwner.collectFlow(
    flow: Flow<T>,
    crossinline collector: suspend (T) -> Unit
) {
    lifecycleScope.launchWhenStarted {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.catch { e -> e.printStackTrace() }
                .collect {
                    collector(it)
                }
        }
    }
}

inline fun <T> LifecycleOwner.collectLatestFlow(
    flow: Flow<T>,
    crossinline collector: suspend (T) -> Unit
) {
    lifecycleScope.launchWhenStarted {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.catch { e -> e.printStackTrace() }
                .collectLatest {
                    collector(it)
                }
        }
    }
}

fun <T> Flow<NetworkResource<T>>.mapToState(): Flow<State<T>> = map { resource ->
    State.fromResource(resource)
}

