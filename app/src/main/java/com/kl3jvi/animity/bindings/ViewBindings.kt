package com.kl3jvi.animity.bindings

import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.request.CachePolicy
import coil.size.Precision
import coil.size.Scale
import coil.transform.CircleCropTransformation
import com.kl3jvi.animity.utils.hide
import com.kl3jvi.animity.utils.show

object ViewBindings {

    @JvmStatic
    @BindingAdapter("image")
    fun setImage(image: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            image.load(url) {
                crossfade(true)
                diskCachePolicy(CachePolicy.ENABLED)
                precision(Precision.EXACT)
                scale(Scale.FILL)
            }
        }
    }

    @JvmStatic
    @BindingAdapter("avatarImage")
    fun setAvatarImage(image: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            image.load(url) {
                crossfade(true)
                diskCachePolicy(CachePolicy.ENABLED)
                transformations(CircleCropTransformation())
            }
        }
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, value: Boolean) {
        if (value)
            view.show()
        else view.hide()
    }
}