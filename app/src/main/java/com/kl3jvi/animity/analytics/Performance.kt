package com.kl3jvi.animity.analytics

import com.google.firebase.perf.FirebasePerformance
import javax.inject.Inject

class Performance @Inject constructor(
    val firebasePerformance: FirebasePerformance,
) {
    inline fun <T> measureAndTrace(
        traceName: String,
        block: () -> T,
    ): T {
        val trace = firebasePerformance.newTrace(traceName)
        trace.start()
        val result = block()
        trace.stop()
        return result
    }
}
