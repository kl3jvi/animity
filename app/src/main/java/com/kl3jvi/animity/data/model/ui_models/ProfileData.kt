package com.kl3jvi.animity.data.model.ui_models

import androidx.paging.PagingData

data class ProfileData(
    val userData: User = User(),
    val profileRow: List<ProfileRow> = emptyList(),
    val followersAndFollowing: PagingData<Pair<List<User>, List<User>>> = PagingData.empty(),
    val followState: Pair<String, String> = "Follow" to "",
)
