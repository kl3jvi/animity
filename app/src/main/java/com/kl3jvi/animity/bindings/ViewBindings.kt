package com.kl3jvi.animity.bindings

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.databinding.BindingAdapter
import coil.load
import coil.transform.CircleCropTransformation
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.kl3jvi.animity.data.model.ui_models.Genre
import com.kl3jvi.animity.di.GlideApp
import com.kl3jvi.animity.utils.hide
import com.kl3jvi.animity.utils.show

object ViewBindings {

    /**
     * A binding adapter that loads an image from a url into an ImageView.
     *
     * @param image ImageView - The ImageView that we want to bind the image to.
     * @param url The URL of the image to load.
     */
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

    /**
     * "When the list_to_string attribute is set on a TextView, join the list of Genre objects into a
     * comma-separated string and set the text of the TextView to that string."
     *
     * The @BindingAdapter annotation tells the compiler that this function is a binding adapter. The
     * @JvmStatic annotation tells the compiler to make this function a static function in the
     * generated Java class. The first parameter is the name of the attribute that will trigger the
     * binding adapter. The second parameter is the type of the attribute. In this case, the attribute
     * is a list of Genre objects. The third parameter is the name of the View that the attribute will
     * be set on. In this case, the attribute will be set on a TextView
     *
     * @param textView TextView - The view that we are binding to.
     * @param list List<Genre> - this is the list of genres that we want to join into a string
     */
    @JvmStatic
    @BindingAdapter("list_to_string")
    fun joinList(textView: TextView, list: List<Genre>) {
        textView.text = list.joinToString { it.name }
    }

    /**
     * "Load an image into an ImageView, using a placeholder and error image if the image fails to
     * load."
     *
     * The function is a bit more complicated than that, but it's still pretty simple
     *
     * @param imageView ImageView - The ImageView to load the image into
     * @param imageUrl The URL of the image to load.
     * @param placeholder The drawable to be used as a placeholder until the image is loaded.
     * @param error Drawable?
     */
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

    /**
     * If the value is true, show the view, otherwise hide it.
     *
     * @param view View - The view you want to change the visibility of
     * @param value The value that will be passed to the binding adapter.
     */
    @JvmStatic
    @BindingAdapter("android:visibility")
    fun setVisibility(view: View, value: Boolean) {
        if (value) view.show()
        else view.hide()
    }


    /**
     * "If the isFiller boolean is true, set the background color of the CardView to #2B2C30, otherwise
     * set it to #17293F."
     *
     * The @BindingAdapter annotation is what tells the compiler that this function is a binding
     * adapter. The first parameter is the name of the attribute that we'll be using in our XML layout.
     * The second parameter is the type of the attribute. In this case, it's a boolean
     *
     * @param view CardView - The view that we're binding to.
     * @param isFiller Boolean - This is the parameter that we will pass to the binding adapter.
     */
    @JvmStatic
    @BindingAdapter("isFiller")
    fun changeFillerBg(view: CardView, isFiller: Boolean) {
        view.setCardBackgroundColor(Color.parseColor(if (isFiller) "#2B2C30" else "#17293F"))
    }


}