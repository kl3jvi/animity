package com.kl3jvi.animity.data.mapper

import com.kl3jvi.animity.NotificationsQuery
import com.kl3jvi.animity.data.model.ui_models.AniListMedia
import com.kl3jvi.animity.data.model.ui_models.FuzzyDate
import com.kl3jvi.animity.data.model.ui_models.Genre
import com.kl3jvi.animity.data.model.ui_models.MediaCoverImage
import com.kl3jvi.animity.data.model.ui_models.MediaTitle
import com.kl3jvi.animity.data.model.ui_models.Notification
import com.kl3jvi.animity.data.model.ui_models.User
import com.kl3jvi.animity.data.model.ui_models.UserAvatar
import com.kl3jvi.animity.data.paging.NotificationType
import com.kl3jvi.animity.data.paging.PagingDataItem

fun NotificationsQuery.Data.convert(): List<PagingDataItem> {
    val notificationsByType = mutableMapOf<String, MutableList<Notification>>()
    page?.notifications?.forEach { notification ->
        val type = when {
            notification?.onAiringNotification != null -> "Airing"
            notification?.onFollowingNotification != null -> "Following"
            notification?.onActivityLikeNotification != null ||
                notification?.onActivityMessageNotification != null ||
                notification?.onActivityMentionNotification != null ||
                notification?.onActivityReplyNotification != null -> "Activity"

            notification?.onThreadCommentMentionNotification != null ||
                notification?.onThreadCommentReplyNotification != null -> "Threads"

            else -> "Unknown"
        }
        val notificationList = notificationsByType.getOrPut(type) { mutableListOf() }
        notificationList.add(
            when (type) {
                "Airing" -> notification?.onAiringNotification!!.toNotification()
                "Following" -> notification?.onFollowingNotification!!.toNotification()
                "Activity" -> {
                    val activityTypeNotification = when {
                        notification?.onActivityLikeNotification != null -> notification.onActivityLikeNotification.toNotification()
                        notification?.onActivityMessageNotification != null -> notification.onActivityMessageNotification.toNotification()
                        notification?.onActivityMentionNotification != null -> notification.onActivityMentionNotification.toNotification()
                        notification?.onActivityReplyNotification != null -> notification.onActivityReplyNotification.toNotification()
                        else -> Notification(type = NotificationType.Unknown)
                    }
                    activityTypeNotification
                }

                "Threads" -> {
                    val threadTypeNotification = when {
                        notification?.onThreadCommentMentionNotification != null -> notification.onThreadCommentMentionNotification.toNotification()
                        notification?.onThreadCommentReplyNotification != null -> notification.onThreadCommentReplyNotification.toNotification()
                        else -> Notification(type = NotificationType.Unknown)
                    }
                    threadTypeNotification
                }

                else -> Notification(type = NotificationType.Unknown)
            }
        )
    }

    val items = mutableListOf<PagingDataItem>()

    notificationsByType.forEach { (type, notificationList) ->
        items.add(PagingDataItem.HeaderItem(type))
        notificationList.forEach { notification ->
            items.add(PagingDataItem.NotificationItem(notification))
        }
    }

    return items
}

private fun NotificationsQuery.OnThreadCommentReplyNotification.toNotification() = Notification(
    id = this.id,
    episode = null,
    user = User(
        id = this.user?.id ?: 0,
        name = this.user?.name.orEmpty(),
        avatar = UserAvatar(
            this.user?.avatar?.large.orEmpty(),
            this.user?.avatar?.medium.orEmpty()
        )
    ),
    contexts = listOf(this.context),
    type = NotificationType.Threads
)

private fun NotificationsQuery.OnThreadCommentMentionNotification.toNotification() = Notification(
    id = this.id,
    episode = null,
    user = User(
        id = this.user?.id ?: 0,
        name = this.user?.name.orEmpty(),
        avatar = UserAvatar(
            this.user?.avatar?.large.orEmpty(),
            this.user?.avatar?.medium.orEmpty()
        )
    ),
    contexts = listOf(this.context),
    type = NotificationType.Threads
)

private fun NotificationsQuery.OnActivityMentionNotification.toNotification() = Notification(
    id = this.id,
    episode = null,
    user = User(
        id = this.user?.id ?: 0,
        name = this.user?.name.orEmpty(),
        avatar = UserAvatar(
            this.user?.avatar?.large.orEmpty(),
            this.user?.avatar?.medium.orEmpty()
        )
    ),
    contexts = listOf(this.context),
    type = NotificationType.Activity(userId)
)

private fun NotificationsQuery.OnActivityReplyNotification.toNotification() = Notification(
    id = this.id,
    episode = null,
    user = User(
        id = this.user?.id ?: 0,
        name = this.user?.name.orEmpty(),
        avatar = UserAvatar(
            this.user?.avatar?.large.orEmpty(),
            this.user?.avatar?.medium.orEmpty()
        )
    ),
    contexts = listOf(this.context),
    type = NotificationType.Activity(userId)
)

private fun NotificationsQuery.OnAiringNotification.toNotification() = Notification(
    id = this.id,
    episode = this.episode,
    contexts = this.contexts,
    media = this.media.convert(),
    type = NotificationType.Airing
)

private fun NotificationsQuery.OnFollowingNotification.toNotification() = Notification(
    id = this.id,
    episode = null,
    user = User(
        id = this.user?.id ?: 0,
        name = this.user?.name.orEmpty(),
        avatar = UserAvatar(
            this.user?.avatar?.large.orEmpty(),
            this.user?.avatar?.medium.orEmpty()
        )
    ),
    contexts = listOf(this.context),
    type = NotificationType.Following(this.userId)
)

private fun NotificationsQuery.OnActivityLikeNotification.toNotification() = Notification(
    id = this.id,
    episode = null,
    user = User(
        id = this.user?.id ?: 0,
        name = this.user?.name.orEmpty(),
        avatar = UserAvatar(
            this.user?.avatar?.large.orEmpty(),
            this.user?.avatar?.medium.orEmpty()
        )
    ),
    contexts = listOf(this.context),
    type = NotificationType.Activity(userId)
)

private fun NotificationsQuery.OnActivityMessageNotification.toNotification() = Notification(
    id = this.id,
    episode = null,
    user = User(
        id = this.user?.id ?: 0,
        name = this.user?.name.orEmpty(),
        avatar = UserAvatar(
            this.user?.avatar?.large.orEmpty(),
            this.user?.avatar?.medium.orEmpty()
        )
    ),
    contexts = listOf(this.context),
    type = NotificationType.Activity(userId)
)

private fun NotificationsQuery.Media?.convert(): AniListMedia {
    return AniListMedia(
        idAniList = this?.homeMedia?.id ?: 0,
        idMal = this?.homeMedia?.idMal,
        title = MediaTitle(userPreferred = this?.homeMedia?.title?.userPreferred.orEmpty()),
        type = this?.homeMedia?.type,
        format = this?.homeMedia?.format,
        isFavourite = this?.homeMedia?.isFavourite ?: false,
        streamingEpisode = this?.homeMedia?.streamingEpisodes?.mapNotNull { it.convert() },
        nextAiringEpisode = this?.homeMedia?.nextAiringEpisode?.airingAt,
        status = this?.homeMedia?.status,
        description = this?.homeMedia?.description.orEmpty(),
        startDate = if (this?.homeMedia?.startDate?.year != null) {
            FuzzyDate(
                this.homeMedia.startDate.year,
                this.homeMedia.startDate.month,
                this.homeMedia.startDate.day
            )
        } else {
            null
        },
        coverImage = MediaCoverImage(
            this?.homeMedia?.coverImage?.extraLarge.orEmpty(),
            this?.homeMedia?.coverImage?.large.orEmpty(),
            this?.homeMedia?.coverImage?.medium.orEmpty()
        ),
        bannerImage = this?.homeMedia?.bannerImage.orEmpty(),
        genres = this?.homeMedia?.genres?.mapNotNull { Genre(name = it.orEmpty()) } ?: emptyList(),
        averageScore = this?.homeMedia?.averageScore ?: 0,
        favourites = this?.homeMedia?.favourites ?: 0
    )
}
