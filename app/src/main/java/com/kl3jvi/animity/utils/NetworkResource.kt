package com.kl3jvi.animity.utils

import com.kl3jvi.animity.data.model.ui_models.AnimeInfoModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart

/* A NetworkResource is either a Success or a Failed */

/* `T` is a generic type. It can be anything. */
/* `<T>` is a generic type. It can be anything. */
//sealed class NetworkResource<T> {
//    class Success<T>(val data: T) : NetworkResource<T>()
//    class Failed<T>(val message: String) : NetworkResource<T>()
//}


sealed interface Result<out T>  {
    data class Success<T>(val data: T) : Result<T>
    data class Error(val exception: Throwable? = null) : Result<Nothing>
    object Loading : Result<Nothing>
}

/* Converting a Flow<T> to a Flow<Result<T>>. */
fun <T> Flow<T>.asResult(): Flow<Result<T>> {
    return map<T, Result<T>> {
        Result.Success(it)
    }.onStart { emit(Result.Loading) }
        .catch { emit(Result.Error(it)) }
}
