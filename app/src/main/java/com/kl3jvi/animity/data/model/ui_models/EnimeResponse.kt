package com.kl3jvi.animity.data.model.ui_models

import com.google.gson.annotations.SerializedName

data class EnimeResponse(
    @SerializedName("episodes")
    val episodes: List<EnimeEpisode>,
)

data class EnimeEpisode(
    @SerializedName("number")
    val number: Int,
    @SerializedName("sources")
    val sources: List<Source>,
    @SerializedName("title")
    val title: String,
)

data class Source(
    @SerializedName("id")
    val id: String,
    @SerializedName("target")
    val target: String,
)
