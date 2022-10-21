package com.kl3jvi.animity.util

import app.cash.turbine.test
import com.kl3jvi.animity.utils.Result
import com.kl3jvi.animity.utils.asResult
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ResultKtTest {

    @Test
    fun Result_catches_errors() = runTest {
        flow {
            emit("Animes are the best")
            throw Exception("Test Finished")
        }.asResult().test {
            assertEquals(Result.Loading, awaitItem())
            assertEquals(Result.Success("Animes are the best"), awaitItem())

            when (val errorResult = awaitItem()) {
                is Result.Error -> assertEquals(
                    "Test Finished",
                    errorResult.exception?.message
                )
                Result.Loading,
                is Result.Success -> throw IllegalStateException(
                    "The flow should have emitted an Error Result"
                )
            }
            awaitComplete()
        }
    }
}
