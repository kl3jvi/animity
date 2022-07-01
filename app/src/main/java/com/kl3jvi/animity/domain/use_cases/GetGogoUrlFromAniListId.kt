package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.domain.repositories.fragment_repositories.FavoriteRepository
import javax.inject.Inject

class GetGogoUrlFromAniListId @Inject constructor(
    private val favoritesRepository: FavoriteRepository
) {
    operator fun invoke(id: Int) = favoritesRepository.getGogoUrlFromAniListId(id)

}