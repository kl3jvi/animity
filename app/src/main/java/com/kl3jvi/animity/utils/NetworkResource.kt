package com.kl3jvi.animity.utils

/* A NetworkResource is either a Success or a Failed */

/* `T` is a generic type. It can be anything. */
/* `<T>` is a generic type. It can be anything. */
sealed class NetworkResource<T> {
    class Success<T>(val data: T) : NetworkResource<T>()
    class Failed<T>(val message: String) : NetworkResource<T>()
}

