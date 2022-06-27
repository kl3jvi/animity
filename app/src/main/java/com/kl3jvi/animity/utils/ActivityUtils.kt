package com.kl3jvi.animity.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import androidx.annotation.LayoutRes
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle

/**
 * Extensions for simpler launching of Activities
 */
inline fun <reified T : Any> Activity.launchActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : Any> Context.launchActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivity(intent, options)
}

inline fun <reified T : Any> newIntent(context: Context): Intent =
    Intent(context, T::class.java)


fun Fragment.createFragmentMenu(
    @LayoutRes menuLayout: Int,
    selectedItem: (menuItem: MenuItem) -> Boolean
) {
    val menuHost = requireActivity()
    menuHost.addMenuProvider(object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(menuLayout, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            return selectedItem(menuItem)
        }
    }, viewLifecycleOwner, Lifecycle.State.RESUMED)
}




