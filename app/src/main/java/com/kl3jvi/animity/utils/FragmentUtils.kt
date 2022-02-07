package com.kl3jvi.animity.utils

import androidx.fragment.app.Fragment
import com.kl3jvi.animity.ui.activities.main.MainActivity

fun Fragment.isGuestLogin(): Boolean {
    return (activity as MainActivity).isGuestLogin
}