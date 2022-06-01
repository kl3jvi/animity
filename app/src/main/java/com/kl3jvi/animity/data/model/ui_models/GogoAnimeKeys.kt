package com.kl3jvi.animity.data.model.ui_models

import com.google.gson.annotations.SerializedName


data class GogoAnimeKeys(
    @SerializedName("iv")
    val iv: String = "",
    @SerializedName("key")
    val key: String = "",
    @SerializedName("second_key")
    val secondKey: String = ""
)