package com.kl3jvi.animity.domain.repositories.activity_repositories

import com.kl3jvi.animity.utils.Constants.Companion.getNetworkHeader
import com.kl3jvi.animity.utils.parser.Parser
import kotlinx.coroutines.flow.Flow

interface PlayerRepository {
    val parser: Parser

    fun getMediaUrl(
        header: Map<String, String> = getNetworkHeader(),
        url: String
    ): Flow<List<String>>

}