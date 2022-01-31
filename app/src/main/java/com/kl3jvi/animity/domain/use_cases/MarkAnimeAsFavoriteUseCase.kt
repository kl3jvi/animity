package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.repository.fragment_repositories.UserRepositoryImpl
import javax.inject.Inject

class MarkAnimeAsFavoriteUseCase @Inject constructor(
    private val userRepositoryImpl: UserRepositoryImpl
) {
    operator fun invoke() {
//        userRepositoryImpl.markAnimeAsFavorite()
    }
}