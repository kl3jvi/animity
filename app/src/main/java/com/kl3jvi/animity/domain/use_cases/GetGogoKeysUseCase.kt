package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.domain.repositories.fragment_repositories.HomeRepository
import com.kl3jvi.animity.utils.NetworkResource
import com.kl3jvi.animity.utils.logError
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetGogoKeysUseCase @Inject constructor(
    private val homeRepository: HomeRepository,
) {
    suspend operator fun invoke() = flow {
        try {
            val response = homeRepository.getEncryptionKeys()
            emit(NetworkResource.Success(response))
        } catch (e: Exception) {
            logError(e)
            emit(NetworkResource.Failed("Error Occurred"))
        }
    }
}