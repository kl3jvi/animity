package com.kl3jvi.animity.domain.use_cases

import com.kl3jvi.animity.SessionQuery
import com.kl3jvi.animity.data.datasource.UserDataSourceImpl
import javax.inject.Inject

class GetUserSessionUseCase @Inject constructor(
    private val user: UserDataSourceImpl
) {
    suspend operator fun invoke(): SessionQuery.Data? {
        return user.getSessionForUser().data
    }
}