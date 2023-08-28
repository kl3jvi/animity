package com.kl3jvi.animity.data.mapper

import com.apollographql.apollo3.api.ApolloResponse
import com.kl3jvi.animity.ToggleFollowUserMutation

fun ApolloResponse<ToggleFollowUserMutation.Data>.convert(): Pair<String, String> {
    /**
     * If the authenticated user if following this user
     */
    val amIFollowingHim = this.data?.follow?.isFollowing ?: false
    val amIFollowingHimResult = if (amIFollowingHim) "Following" else "Follow"

    /**
     * If this user if following the authenticated user
     */
    val isHeFollowingMe = this.data?.follow?.isFollower ?: false
    val isHeFollowingMeResult = if (isHeFollowingMe) "${this.data?.follow?.name} follows you" else "${this.data?.follow?.name} does not follows you"

    return amIFollowingHimResult to isHeFollowingMeResult
}
