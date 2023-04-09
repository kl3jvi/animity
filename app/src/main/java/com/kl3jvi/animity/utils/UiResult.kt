package com.kl3jvi.animity.utils

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn

// Ui Result
sealed interface UiResult<out T> {
    data class Success<T>(val data: T) : UiResult<T>
    data class Error(val throwable: Throwable) : UiResult<Nothing>
    object Loading : UiResult<Nothing>
}

fun <T> Flow<T>.mapToUiState(
    scope: CoroutineScope
): StateFlow<UiResult<T>> {
    return map<T, UiResult<T>> {
        UiResult.Success(it)
    }.onStart {
        emit(UiResult.Loading)
    }.catch {
        emit(UiResult.Error(it))
    }.stateIn(
        scope = scope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = UiResult.Loading
    )
}
