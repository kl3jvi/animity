package com.kl3jvi.animity.data.model.ui_models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class MalSyncResponse(
    @SerialName("aniId")
    val aniId: Int,
    @SerialName("aniUrl")
    val aniUrl: String,
    @SerialName("identifier")
    val identifier: String,
    @SerialName("image")
    val image: String,
    @SerialName("malId")
    val malId: Int,
    @SerialName("malUrl")
    val malUrl: String,
    @SerialName("page")
    val page: String,
    @SerialName("title")
    val title: String,
    @SerialName("type")
    val type: String,
    @SerialName("url")
    val url: String
)