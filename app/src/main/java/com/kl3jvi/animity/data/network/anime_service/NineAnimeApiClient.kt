package com.kl3jvi.animity.data.network.anime_service

import com.apollographql.apollo3.ApolloClient
import javax.inject.Inject

class NineAnimeApiClient @Inject constructor(
    private val nineAnimeService: NineAnimeService,
    private val apolloClient: ApolloClient
)