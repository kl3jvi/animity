package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.data.mapper.ProfileData
import com.kl3jvi.animity.data.repository.fragment_repositories.ProfileRepositoryImpl
import com.kl3jvi.animity.utils.NetworkResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class GetUserDataUseCase @Inject constructor(
    private val repository: ProfileRepositoryImpl,
    private val ioDispatcher: CoroutineDispatcher
) {
    operator fun invoke(id: Int?): Flow<NetworkResource<ProfileData>> {
        return repository.getProfileData(id).flowOn(ioDispatcher)
    }
}

