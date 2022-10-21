package com.kl3jvi.animity.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

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
