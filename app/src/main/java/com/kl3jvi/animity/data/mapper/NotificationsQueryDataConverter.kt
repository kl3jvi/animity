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

inline fun <T, R : Any> Iterable<T>.flatMapNotNull(transform: (T) -> R?): List<R> {
    return flatMapTo(mutableListOf()) { transform(it)?.let { listOf(it) } ?: emptyList() }
}

fun NotificationsQuery.Data.convert(): List<Notification> {
    val listOfNotifications = mutableListOf<Notification>()
    page?.notifications?.forEach { notification ->
        when {
            notification?.onAiringNotification != null -> listOfNotifications.add(notification.onAiringNotification.toNotification())

            notification?.onFollowingNotification != null -> listOfNotifications.add(notification.onFollowingNotification.toNotification())

            notification?.onActivityLikeNotification != null -> listOfNotifications.add(notification.onActivityLikeNotification.toNotification())

            notification?.onActivityMessageNotification != null -> listOfNotifications.add(
                notification.onActivityMessageNotification.toNotification()
            )

            notification?.onActivityMentionNotification != null -> listOfNotifications.add(
                notification.onActivityMentionNotification.toNotification()
            )

            notification?.onActivityReplyNotification != null -> listOfNotifications.add(
                notification.onActivityReplyNotification.toNotification()
            )

            notification?.onThreadCommentMentionNotification != null -> listOfNotifications.add(
                notification.onThreadCommentMentionNotification.toNotification()
            )

            notification?.onThreadCommentReplyNotification != null -> listOfNotifications.add(
                notification.onThreadCommentReplyNotification.toNotification()
            )
        }
    }
    return listOfNotifications
}

private fun NotificationsQuery.OnThreadCommentReplyNotification.toNotification() = Notification(
    id = this.id, episode = null, user = User(
        id = this.user?.id ?: 0, name = this.user?.name.orEmpty(), avatar = UserAvatar(
            this.user?.avatar?.large.orEmpty(), this.user?.avatar?.medium.orEmpty()
        )
    ), contexts = listOf(this.context)
)

private fun NotificationsQuery.OnThreadCommentMentionNotification.toNotification() = Notification(
    id = this.id, episode = null, user = User(
        id = this.user?.id ?: 0, name = this.user?.name.orEmpty(), avatar = UserAvatar(
            this.user?.avatar?.large.orEmpty(), this.user?.avatar?.medium.orEmpty()
        )
    ), contexts = listOf(this.context)
)

private fun NotificationsQuery.OnActivityMentionNotification.toNotification() = Notification(
    id = this.id, episode = null, user = User(
        id = this.user?.id ?: 0, name = this.user?.name.orEmpty(), avatar = UserAvatar(
            this.user?.avatar?.large.orEmpty(), this.user?.avatar?.medium.orEmpty()
        )
    ), contexts = listOf(this.context)
)

private fun NotificationsQuery.OnActivityReplyNotification.toNotification() = Notification(
    id = this.id, episode = null, user = User(
        id = this.user?.id ?: 0, name = this.user?.name.orEmpty(), avatar = UserAvatar(
            this.user?.avatar?.large.orEmpty(), this.user?.avatar?.medium.orEmpty()
        )
    ), contexts = listOf(this.context)
)

private fun NotificationsQuery.OnAiringNotification.toNotification() = Notification(
    id = this.id, episode = this.episode, contexts = this.contexts, media = this.media.convert()
)

private fun NotificationsQuery.OnFollowingNotification.toNotification() = Notification(
    id = this.id, episode = null, user = User(
        id = this.user?.id ?: 0, name = this.user?.name.orEmpty(), avatar = UserAvatar(
            this.user?.avatar?.large.orEmpty(), this.user?.avatar?.medium.orEmpty()
        )
    ), contexts = listOf(this.context)
)

private fun NotificationsQuery.OnActivityLikeNotification.toNotification() = Notification(
    id = this.id, episode = null, user = User(
        id = this.user?.id ?: 0, name = this.user?.name.orEmpty(), avatar = UserAvatar(
            this.user?.avatar?.large.orEmpty(), this.user?.avatar?.medium.orEmpty()
        )
    ), contexts = listOf(this.context)
)

private fun NotificationsQuery.OnActivityMessageNotification.toNotification() = Notification(
    id = this.id, episode = null, user = User(
        id = this.user?.id ?: 0, name = this.user?.name.orEmpty(), avatar = UserAvatar(
            this.user?.avatar?.large.orEmpty(), this.user?.avatar?.medium.orEmpty()
        )
    ), contexts = listOf(this.context)
)

private fun NotificationsQuery.Media?.convert(): AniListMedia {
    return AniListMedia(idAniList = this?.homeMedia?.id ?: 0,
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
        favourites = this?.homeMedia?.favourites ?: 0)
}
