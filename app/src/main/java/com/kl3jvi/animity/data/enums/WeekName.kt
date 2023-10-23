package com.kl3jvi.animity.data.enums

import java.util.Calendar

enum class WeekName {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY,
    ;

    companion object {
        fun WeekName.computeEpochTimesForDay(): Pair<Int, Int> {
            val calendar =
                Calendar.getInstance().apply {
                    set(Calendar.HOUR_OF_DAY, 0)
                    set(Calendar.MINUTE, 0)
                    set(Calendar.SECOND, 0)
                    set(Calendar.MILLISECOND, 0)

                    val daysDifference = ordinal - (get(Calendar.DAY_OF_WEEK) - 2 + 7) % 7
                    add(Calendar.DAY_OF_MONTH, daysDifference)
                }

            val startTime = (calendar.timeInMillis / 1000).toInt() // Start of the selected day
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val endTime = (calendar.timeInMillis / 1000).toInt() - 1 // End of the selected day

            return Pair(startTime, endTime)
        }
    }
}
