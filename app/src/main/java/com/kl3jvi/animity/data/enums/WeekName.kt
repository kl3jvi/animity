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
            val calendar = Calendar.getInstance().apply {
                set(Calendar.HOUR_OF_DAY, 0)
                set(Calendar.MINUTE, 0)
                set(Calendar.SECOND, 0)
                set(Calendar.MILLISECOND, 0)

                while (get(Calendar.DAY_OF_WEEK) - 1 != ordinal) {
                    add(Calendar.DAY_OF_MONTH, 1)
                }
            }

            val startTime = (calendar.timeInMillis / 1000).toInt() // Start of the selected day
            calendar.add(Calendar.DAY_OF_MONTH, 1)
            val endTime = (calendar.timeInMillis / 1000).toInt() // End of the selected day

            return Pair(startTime, endTime)
        }
    }
}
