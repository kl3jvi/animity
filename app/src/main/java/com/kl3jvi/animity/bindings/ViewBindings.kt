package com.kl3jvi.animity.bindings

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kl3jvi.animity.R
import com.kl3jvi.animity.di.GlideApp
import com.kl3jvi.animity.utils.hide
import com.kl3jvi.animity.utils.show

object ViewBindings {

    @JvmStatic
    @BindingAdapter("image")
    fun setImage(image: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            GlideApp.with(image.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(image)
        }
    }

    @JvmStatic
    @BindingAdapter("drawable")
    fun setImageDrawable(image: ImageView, drawable: Drawable?) {
        if (drawable != null) {
            GlideApp.with(image.context)
                .load(R.drawable.ic_play_tv)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(image)
        }
    }

    @JvmStatic
    @BindingAdapter("avatarImage")
    fun setAvatarImage(image: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            image.load(url) {
                crossfade(true)
                transformations(CircleCropTransformation())
            }
        }
    }

    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, value: Boolean) {
        if (value) view.show()
        else view.hide()
    }
}