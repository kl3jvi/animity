package com.kl3jvi.animity.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch


/* A function that observes a LiveData and calls the observer function when the LiveData changes. */
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

inline fun <T> LifecycleOwner.collectFlow(
    flow: Flow<T>,
    crossinline collector: suspend (T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.catch { e -> logError(e) }
                .collect {
                    collector(it)
                }
        }
    }
}

inline fun <T> Fragment.collectLatestFlow(
    flow: Flow<T>,
    crossinline collector: suspend (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.catch { e -> e.printStackTrace() }
                .collectLatest {
                    collector(it)
                }
        }
    }
}

/* A function that takes a Flow of NetworkResource and returns a Flow of State. */
fun <T> Flow<NetworkResource<T>>.mapToState(): Flow<State<T>> = map { resource ->
    State.fromResource(resource)
}

