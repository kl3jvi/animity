package com.kl3jvi.animity.domain.use_cases

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.SearchAnimeQuery
import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import com.kl3jvi.animity.type.*
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetAnimeDetailsFromAnilistUseCase @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(
        page: Int,
        search: String?,
        sort: List<MediaSort>?,
        formatIn: List<MediaFormat>?,
        statusIn: List<MediaStatus>?,
        sourceIn: List<MediaSource>?,
        countryOfOrigin: String?,
        season: MediaSeason?,
        startDateGreater: Int?,
        startDateLesser: Int?,
        isAdult: Boolean?,
        onList: Boolean?,
        genreIn: List<String>?,
        genreNotIn: List<String>?,
        minimumTagRank: Int?,
        tagIn: List<String>?,
        tagNotIn: List<String>?,
        licensedByIn: List<String>?,
        episodesGreater: Int?,
        episodesLesser: Int?,
        durationGreater: Int?,
        durationLesser: Int?,
        averageScoreGreater: Int?,
        averageScoreLesser: Int?,
        popularityGreater: Int?,
        popularityLesser: Int?
    ): Flow<ApolloResponse<SearchAnimeQuery.Data>> {
        return userRepositoryImpl.searchAnime(
            page,
            search,
            sort,
            formatIn,
            statusIn,
            sourceIn,
            countryOfOrigin,
            season,
            startDateGreater,
            startDateLesser,
            isAdult,
            onList,
            genreIn,
            genreNotIn,
            minimumTagRank,
            tagIn,
            tagNotIn,
            licensedByIn,
            episodesGreater,
            episodesLesser,
            durationGreater,
            durationLesser,
            averageScoreGreater,
            averageScoreLesser,
            popularityGreater,
            popularityLesser
        ).flowOn(ioDispatcher)
    }
}