package com.kl3jvi.animity.utils

import kotlinx.coroutines.flow.*


sealed interface Result<out T> {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    object Loading : Result<Nothing>
}

/* Converting a Flow<T> to a Flow<Result<T>>. */
/**
 * Part of Now In Android google Sample
 */
fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return map<T, Result<T>> {
        Result.Success(it)
    }.onStart {
        emit(Result.Loading)
    }.catch {
        emit(Result.Error(it))
    }
}

