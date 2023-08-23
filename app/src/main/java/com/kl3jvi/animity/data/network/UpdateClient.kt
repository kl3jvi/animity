package com.kl3jvi.animity.data.network

import javax.inject.Inject

class UpdateClient @Inject constructor(
    private val updateService: UpdateService,
) {
    suspend fun getUpdateInfo(): VersionInfo = updateService.getUpdateInfo()
}
