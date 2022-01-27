package com.kl3jvi.animity.domain.repositories.network_repositories

import android.util.Log
import androidx.annotation.MainThread
import com.apollographql.apollo3.api.ApolloResponse
import com.apollographql.apollo3.api.Operation
import com.kl3jvi.animity.utils.NetworkResource
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response

@ExperimentalCoroutinesApi
abstract class NetworkBoundRepository<RESULT> {
    fun asFlow() = flow<NetworkResource<RESULT>> {

        // Fetch remote content and parse body
        val response = fetchFromRemote()
        val body = response.body()
        Log.e("Response", body.toString())

        if (!response.isSuccessful && body == null)
            emit(NetworkResource.Failed<RESULT>(response.message()))
        else if (body != null) emit(
            NetworkResource.Success<RESULT>(body)
        )
    }.catch { e ->
        e.printStackTrace()
        emit(NetworkResource.Failed("Network Error Happened!"))
    }

    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<RESULT>
}


abstract class ApolloNetworkRepository<RESULT : Operation.Data> {

    fun asFlow() = flow<NetworkResource<RESULT>> {

        // Fetch remote content and parse body
        val response = fetchFromRemote()
        val body = response.data
        Log.e("Response", body.toString())

        if (!response.hasErrors() && body == null)
            emit(NetworkResource.Failed<RESULT>(response.errors.toString()))
        else if (body != null) emit(
            NetworkResource.Success<RESULT>(body as RESULT)
        )
    }.catch { e ->
        e.printStackTrace()
        emit(NetworkResource.Failed("Network Error Happened!"))
    }

    @MainThread
    protected abstract suspend fun fetchFromRemote(): ApolloResponse<Operation.Data>
}