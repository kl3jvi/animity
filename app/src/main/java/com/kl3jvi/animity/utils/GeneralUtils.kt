package com.kl3jvi.animity.utils

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.View
import androidx.annotation.AttrRes
import androidx.annotation.ColorInt
import androidx.core.content.res.use

fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

fun logError(throwable: Throwable) {
    Log.e("ApiError", "-------------------------------------------------------------------")
    Log.e("ApiError", "safeApiCall: " + throwable.localizedMessage)
    Log.e("ApiError", "safeApiCall: " + throwable.message)
    throwable.printStackTrace()
    Log.e("ApiError", "-------------------------------------------------------------------")
}

fun logMessage(string: String?) {
    Log.e("Error Happened", "-------------------------------------------------------------------")
    Log.e("Error Happened", "--->: ${string.orEmpty()}")
    Log.e("Error Happened", "-------------------------------------------------------------------")
}

@ColorInt
@SuppressLint("Recycle")
fun Context.themeColor(
    @AttrRes themeAttrId: Int
): Int {
    return obtainStyledAttributes(
        intArrayOf(themeAttrId)
    ).use {
        it.getColor(0, Color.MAGENTA)
    }
}

