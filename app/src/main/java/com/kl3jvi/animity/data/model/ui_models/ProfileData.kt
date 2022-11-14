package com.kl3jvi.animity.data.model.ui_models

data class ProfileData(
    val userData: User = User(),
    val profileRow: List<ProfileRow> = emptyList()
)
