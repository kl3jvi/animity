package com.kl3jvi.animity.data.model.ui_models.test

import com.google.gson.annotations.SerializedName


data class DetailedAnimeInfo(
    @SerializedName("Pages")
    val pages: Pages?,
)

data class Pages(
    @SerializedName("Gogoanime")
    val data: Map<String, SubInfo>?
)

data class SubInfo(
    @SerializedName("active")
    val active: Boolean?,
    @SerializedName("actor")
    val actor: Any?,
    @SerializedName("aniId")
    val aniId: Int?,
    @SerializedName("createdAt")
    val createdAt: String?,
    @SerializedName("deletedAt")
    val deletedAt: Any?,
    @SerializedName("hentai")
    val hentai: Boolean?,
    @SerializedName("identifier")
    val identifier: String?,
    @SerializedName("image")
    val image: String?,
    @SerializedName("malId")
    val malId: Int?,
    @SerializedName("page")
    val page: String?,
    @SerializedName("sticky")
    val sticky: Boolean?,
    @SerializedName("title")
    val title: String?,
    @SerializedName("type")
    val type: String?,
    @SerializedName("updatedAt")
    val updatedAt: String?,
    @SerializedName("url")
    val url: String?
)


