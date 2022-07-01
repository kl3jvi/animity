package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.domain.repositories.fragment_repositories.HomeRepository
import javax.inject.Inject

class GetGogoKeysUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke() = homeRepository.getEncryptionKeys()
}