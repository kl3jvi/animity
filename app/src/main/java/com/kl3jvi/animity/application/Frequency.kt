package com.kl3jvi.animity.application

import java.util.concurrent.TimeUnit

/**
 * Indicates how often the work request should be run.
 *
 * @property repeatInterval Long indicating how often the update should happen.
 * @property repeatIntervalTimeUnit The time unit of [repeatInterval].
 */
data class Frequency(
    val repeatInterval: Long,
    val repeatIntervalTimeUnit: TimeUnit,
)
