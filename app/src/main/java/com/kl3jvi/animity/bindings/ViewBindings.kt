package com.kl3jvi.animity.bindings

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kl3jvi.animity.data.model.ui_models.Genre
import com.kl3jvi.animity.data.model.ui_models.GenreModel
import com.kl3jvi.animity.di.GlideApp
import com.kl3jvi.animity.utils.hide
import com.kl3jvi.animity.utils.show

object ViewBindings {

    @JvmStatic
    @BindingAdapter("image")
    fun provideImageBinding(image: ImageView, url: String?) {
        if (!url.isNullOrEmpty()) {
            GlideApp.with(image.context)
                .load(url)
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)
                .into(image)
        }
    }

    @JvmStatic
    @BindingAdapter("list_to_string")
    fun joinList(textView: TextView, list: List<Genre>) {
        textView.text = list.joinToString { it.name }
    }

    @JvmStatic
    @BindingAdapter(value = ["imageUrl", "placeholder", "error"], requireAll = false)
    fun loadImage(
        imageView: ImageView,
        imageUrl: String?,
        placeholder: Drawable?,
        error: Drawable?
    ) {
        imageView
            .load(imageUrl) {
                placeholder(placeholder)
                error(error)
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