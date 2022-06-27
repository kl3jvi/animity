package com.kl3jvi.animity.utils

import android.os.Build
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.TextView
import com.kl3jvi.animity.R
import io.noties.markwon.Markwon


fun View.hide() {
    visibility = View.GONE
}

fun View.show() {
    visibility = View.VISIBLE
}

/**
 * It logs the error message, the localized message, the throwable object, and the stack trace
 *
 * @param throwable The exception that was thrown.
 */
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

/* A function that is used to set the text of a TextView to a HTML string. */
fun TextView.setHtmlText(htmlString: String?) {
    text = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        Html.fromHtml(htmlString, Html.FROM_HTML_MODE_LEGACY)
    } else {
        Html.fromHtml(htmlString)
    }
}

fun TextView.setMarkdownText(string: String) {
    Markwon.create(this.context).setMarkdown(this, string)
}


