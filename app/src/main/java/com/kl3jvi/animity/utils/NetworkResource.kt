package com.kl3jvi.animity.utils

sealed class NetworkResource<T> {
    class Success<T>(val data: T) : NetworkResource<T>()
    class Failed<T>(val message: String) : NetworkResource<T>()
}