package com.kl3jvi.animity.data.model.ui_models

import com.google.gson.annotations.SerializedName


data class ZoroVideoSource(
    @SerializedName("browser")
    val browser: Boolean,
    @SerializedName("id")
    val id: String,
    @SerializedName("priority")
    val priority: Int,
    @SerializedName("referer")
    val referer: String,
    @SerializedName("subtitle")
    val subtitle: String,
    @SerializedName("url")
    val url: String,
    @SerializedName("website")
    val website: String
)