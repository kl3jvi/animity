package com.kl3jvi.animity.bindings

import android.graphics.drawable.Drawable
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.CachePolicy
import android.view.View




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
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, value: Boolean) {
        view.visibility = if (value) View.VISIBLE else View.GONE
    }
}