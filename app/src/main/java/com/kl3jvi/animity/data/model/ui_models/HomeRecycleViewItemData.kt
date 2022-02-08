package com.kl3jvi.animity.data.model.ui_models

data class HomeRecycleViewItemData(
    val headerTitle: String,
    val listOfAnimeMetaModel: List<AnimeMetaModel>,
    val isHorizontalScrollView: Boolean = true
)