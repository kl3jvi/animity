package com.kl3jvi.animity.di

import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule

/* It tells Glide to use the default OkHttp library for making network requests */
@GlideModule
class GlideModule : AppGlideModule()