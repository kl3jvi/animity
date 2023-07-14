package com.kl3jvi.animity.data.network

import com.kl3jvi.animity.data.network.anime_service.base.BaseClient
import javax.inject.Inject

class UpdateClient @Inject constructor(
    private val updateService: UpdateService
) {
    suspend fun getUpdateInfo(): VersionInfo = updateService.getUpdateInfo()
}