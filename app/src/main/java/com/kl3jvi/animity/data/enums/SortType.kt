package com.kl3jvi.animity.data.enums

import com.kl3jvi.animity.type.MediaSort

enum class SortType {
    TITLE,
    START_DATE,
    POPULARITY,
    AVERAGE_SCORE,
    TRENDING,
    FAVOURITES,
    EPISODES;


    fun toMediaSort(): MediaSort {
        return when (this) {
            TITLE -> MediaSort.TITLE_ENGLISH
            START_DATE -> MediaSort.START_DATE
            POPULARITY -> MediaSort.POPULARITY
            AVERAGE_SCORE -> MediaSort.SCORE
            TRENDING -> MediaSort.TRENDING
            FAVOURITES -> MediaSort.FAVOURITES
            EPISODES -> MediaSort.EPISODES
        }
    }
}
