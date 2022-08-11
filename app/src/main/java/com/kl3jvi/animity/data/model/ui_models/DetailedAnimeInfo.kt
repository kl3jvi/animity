package com.kl3jvi.animity.data.model.ui_models

import com.google.gson.annotations.SerializedName


data class DetailedAnimeInfo(
    @SerializedName("Pages")
    val pages: Pages?,
)

data class Pages(
    @SerializedName("Gogoanime")
    val data: Map<String, SubInfo>?
){

}

data class SubInfo(
    @SerializedName("url")
    val url: String?
)


