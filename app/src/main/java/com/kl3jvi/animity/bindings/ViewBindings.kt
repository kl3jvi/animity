package com.kl3jvi.animity.bindings

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.CachePolicy
import com.kl3jvi.animity.R

object ViewBindings {

    @JvmStatic
    @BindingAdapter("image")
    fun setImage(image: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            image.load(url) {
                crossfade(true)
                diskCachePolicy(CachePolicy.ENABLED)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("downloadStateImg")
    fun setDownloadStateImage(image: ImageView, state: Boolean) {
        image.load(if (state) R.drawable.ic_baseline_file_download_done_24 else R.drawable.ic_outline_arrow_downward_24) {
            crossfade(true)
            diskCachePolicy(CachePolicy.ENABLED)
        }
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, value: Boolean) {
        view.visibility = if (value) View.VISIBLE else View.GONE
    }
}