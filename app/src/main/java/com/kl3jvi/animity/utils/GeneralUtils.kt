package com.kl3jvi.animity.utils

import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView

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
    Log.e("ApiError", "safeApiCall: $throwable")
    throwable.printStackTrace()
    Log.e("ApiError", "-------------------------------------------------------------------")
}

fun logMessage(string: String?) {
    Log.e("Error Happened", "-------------------------------------------------------------------")
    Log.e("Error Happened", "--->: ${string.orEmpty()}")
    Log.e("Error Happened", "-------------------------------------------------------------------")
}

fun TextView.setHtmlText(htmlString: String?) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(htmlString)
    }
}

