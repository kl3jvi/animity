package com.kl3jvi.animity.utils

import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

fun <T> LifecycleOwner.collect(
    flow: Flow<T>,
    collector: suspend (T) -> Unit
) {
    lifecycleScope.launch {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.catch { e -> logError(e) }.collect(collector)
        }
    }
}

fun <T> Fragment.collectLatest(
    flow: Flow<T>,
    collector: suspend (T) -> Unit
) {
    viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        repeatOnLifecycle(Lifecycle.State.STARTED) {
            flow.catch { e -> e.printStackTrace() }.collectLatest(collector)
        }
    }
}

fun <T> Flow<List<T>>.reverseIf(predicate: MutableStateFlow<Boolean>): Flow<List<T>> {
    return combine(predicate) { list, bool ->
        if (bool) list.asReversed() else list
    }
}
