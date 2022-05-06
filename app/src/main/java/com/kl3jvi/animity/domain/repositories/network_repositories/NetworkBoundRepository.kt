package com.kl3jvi.animity.domain.repositories.network_repositories

import android.util.Log
import androidx.annotation.MainThread
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.logError
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import retrofit2.Response

@ExperimentalCoroutinesApi
abstract class NetworkBoundRepository<RESULT> {
    fun asFlow() = flow<NetworkResource<RESULT>> {

        /* Fetching the response from the remote server and then getting the body of the response. */
        val response = fetchFromRemote()
        val body = response.body()
        Log.e("Response", body.toString())

        /* This is checking if the response is successful and if the body is null. If it is, then it
        will emit a failed network resource. If the body is not null, then it will emit a successful
        network resource. */
        if (!response.isSuccessful && body == null)
            emit(NetworkResource.Failed<RESULT>(response.message()))
        else if (body != null) {
            try {
                emit(NetworkResource.Success<RESULT>(body))
            } catch (e: Exception) {
                logError(e)
            }
        }
    /* It's catching any errors that might happen in the flow and then emitting a failed network
    resource. */
    }.catch { e ->
        logError(e)
        emit(NetworkResource.Failed("Network Error Happened!"))
    }

    /**
     * It's a suspend function that returns a Response<RESULT> and is annotated with @MainThread
     */
    @MainThread
    protected abstract suspend fun fetchFromRemote(): Response<RESULT>
}
