package com.kl3jvi.animity.utils

import android.content.Context
import androidx.annotation.StringRes

fun Context.getPreferenceKey(
    @StringRes resourceId: Int,
): String = resources.getString(resourceId)
