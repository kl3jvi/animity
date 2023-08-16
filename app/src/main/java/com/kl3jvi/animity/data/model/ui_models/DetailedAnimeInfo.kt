package com.kl3jvi.animity.data.model.ui_models

import com.google.gson.annotations.SerializedName

data class DetailedAnimeInfo(
    @SerializedName("Sites")
    val pages: Pages?
)

data class Pages(
    @SerializedName("Gogoanime")
    val data: Map<String, SubInfo>?
) {
    fun getGogoUrl(): String {
        return data?.entries?.first()?.value?.url.orEmpty()
    }
}

data class SubInfo(
    @SerializedName("url")
    val url: String?
)
