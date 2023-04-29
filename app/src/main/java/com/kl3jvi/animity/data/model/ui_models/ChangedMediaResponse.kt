package com.kl3jvi.animity.data.model.ui_models

import android.os.Parcelable
import com.kl3jvi.animity.data.mapper.MediaStatusAnimity
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChangedMediaResponse(
    val id: Int?,
    val status: MediaStatusAnimity?
) : Parcelable
