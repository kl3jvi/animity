package com.kl3jvi.animity.data.model.ui_models

import android.content.res.ColorStateList
import android.graphics.Color
import android.os.Parcelable
import com.kl3jvi.animity.type.*
import com.kl3jvi.animity.utils.displayInDayDateTimeFormat
import kotlinx.parcelize.Parcelize

data class HomeData(
    val trendingAnime: List<AniListMedia> = emptyList(),
    val popularAnime: List<AniListMedia> = emptyList(),
    val movies: List<AniListMedia> = emptyList(),
    val review: List<Review> = emptyList()
)

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
    val siteUrl: String = ""
//    val mediaListEntry: MediaList? = null
) : Parcelable {

    fun getGenresToString(): String {
        return if (genres.size < 3) {
            genres.joinToString { it.name }
        } else if (genres.size > 3) {
            genres.subList(0, 2).joinToString { it.name }
        } else {
            genres.joinToString { it.name }
        }
    }

    fun getDateTimeStringFormat(): String? {
        return nextAiringEpisode.takeIf {
            it != null
        }?.run { displayInDayDateTimeFormat(this) }
    }
}

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
