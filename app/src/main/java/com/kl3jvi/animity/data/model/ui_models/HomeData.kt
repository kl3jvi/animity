package com.kl3jvi.animity.data.model.ui_models


import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Parcelable
import com.kl3jvi.animity.type.*
import kotlinx.parcelize.Parcelize
import kotlinx.parcelize.RawValue


data class HomeData(
    val trendingAnime: List<AniListMedia> = listOf(),
    val newAnime: List<AniListMedia> = listOf(),
    val movies: List<AniListMedia> = listOf(),
    val review: List<Review> = listOf()
)

@Parcelize
data class Review(
    val id: Int = 0,
    val userId: Int = 0,
    val mediaId: Int = 0,
    val mediaType: MediaType? = null,
    val summary: String = "",
    val body: String = "",
    val rating: Int = 0,
    val ratingAmount: Int = 0,
    val score: Int = 0,
    val private: Boolean = false,
    val siteUrl: String = "",
    val createdAt: Int = 0,
    val updatedAt: Int = 0,
    val user: User = User(),
    val aniListMedia: AniListMedia = AniListMedia()
) : Parcelable


@Parcelize
data class AniListMedia(
    val idAniList: Int = 0,
    val idMal: Int? = null,
    val title: MediaTitle = MediaTitle(),
    val type: MediaType? = null,
    val format: MediaFormat? = null,
    val status: MediaStatus? = null,
    val nextAiringEpisode: Int? = null,
    val description: String = "",
    val startDate: FuzzyDate? = null,
    val endDate: FuzzyDate? = null,
    val season: MediaSeason? = null,
    val seasonYear: Int? = null,
    val episodes: Int? = null,
    val duration: Int? = null,
    val chapters: Int? = null,
    val volumes: Int? = null,
    val countryOfOrigin: String? = null,
    val isLicensed: Boolean? = null,
    val source: MediaSource? = null,
    val streamingEpisode: List<Episodes>? = null,
//    val trailer: MediaTrailer? = null,
    val coverImage: MediaCoverImage = MediaCoverImage(),
    val bannerImage: String = "",
    val genres: List<Genre> = listOf(),
    val synonyms: List<String> = listOf(),
    val averageScore: Int = 0,
    val meanScore: Int = 0,
    val popularity: Int = 0,
    val trending: Int = 0,
    val favourites: Int = 0,
//    val tags: List<MediaTag> = listOf(),
    var isFavourite: Boolean = false,
    val isAdult: Boolean = false,
//    val nextAiringEpisode: AiringSchedule? = null,
//    val externalLinks: List<MediaExternalLink> = listOf(),
    val siteUrl: String = "",
//    val mediaListEntry: MediaList? = null
) : Parcelable

@Parcelize
data class MediaTitle(
    val romaji: String = "",
    val english: String = "",
    val native: String = "",
    val userPreferred: String = ""
) : Parcelable

@Parcelize
data class Genre(
    val name: String = ""
) : Parcelable

fun Genre.getHexColor(): String {
    return when (name) {
        "Action" -> "#24687B"
        "Adventure" -> "#014037"
        "Comedy" -> "#E6977E"
        "Drama" -> "#7E1416"
        "Ecchi" -> "#7E174A"
        "Fantasy" -> "#989D60"
        "Hentai" -> "#37286B"
        "Horror" -> "#5B1765"
        "Mahou Shoujo" -> "#BF5264"
        "Mecha" -> "#542437"
        "Music" -> "#329669"
        "Mystery" -> "#3D3251"
        "Psychological" -> "#D85C43"
        "Romance" -> "#C02944"
        "Sci-Fi" -> "#85B14B"
        "Slice of Life" -> "#D3B042"
        "Sports" -> "#6B9145"
        "Supernatural" -> "#338074"
        "Thriller" -> "#224C80"
        else -> "#000000"
    }
}

fun String.toStateListColor(): ColorStateList {
    return ColorStateList.valueOf(Color.parseColor(this))
}

@Parcelize
data class User(
    val id: Int = 0,
    val name: String = "",
    val about: String = "",
    val avatar: UserAvatar = UserAvatar(),
    val bannerImage: String = "",
    var isFollowing: Boolean = false,
    val isFollower: Boolean = false,
    val isBlocked: Boolean = false,
    val mediaListOptions: @RawValue MediaListOptions = MediaListOptions(),
    val favourites: @RawValue Favourites = Favourites(),
    val statistics: @RawValue UserStatisticTypes = UserStatisticTypes(),
    val unreadNotificationCount: Int = 0,
    val siteUrl: String = "",
    val donatorTier: Int = 0,
    val donatorBadge: String = "",
    val moderatorRoles: List<ModRole> = listOf()
) : Parcelable

@Parcelize
data class UserAvatar(
    val large: String = "",
    val medium: String = ""
) : Parcelable {
    fun getImageUrl(): String {
        return large
    }
}

@Parcelize
data class FuzzyDate(
    val year: Int? = null,
    val month: Int? = null,
    val day: Int? = null
) : Parcelable {
    fun isNull(): Boolean {
        return year == null || month == null || day == null
    }

    fun getDate(): String {
        return if (!isNull())
            "$year/$month/$day"
        else "Unknown"
    }
}

@Parcelize
data class MediaCoverImage(
    val extraLarge: String = "",
    val large: String = "",
    val medium: String = ""
) : Parcelable

@Parcelize
data class Episodes(
    val title: String? = "",
    val thumbnail: String? = "",
) : Parcelable