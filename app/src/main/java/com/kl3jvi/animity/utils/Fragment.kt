package com.kl3jvi.animity.utils

import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.MenuRes
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.NavController
import androidx.navigation.NavDirections
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.kl3jvi.animity.R

/* A function that is used to navigate to a destination safely. */
fun View.navigateSafe(directions: NavDirections, navOptions: NavOptions? = null) {
    if (canNavigate()) this.findNavController().navigate(directions, navOptions)
}

/* Checking if the current destination is the same as the destination of the fragment. */
fun View.canNavigate(): Boolean {
    val navController = findNavController()
    val destinationIdInNavController = navController.currentDestination?.id
    val destinationIdOfThisFragment =
        getTag(R.id.tag_navigation_destination_id) ?: destinationIdInNavController
    // check that the navigation graph is still in 'this' fragment, if not then the app already navigated:
    return if (destinationIdInNavController == destinationIdOfThisFragment) {
        setTag(R.id.tag_navigation_destination_id, destinationIdOfThisFragment)
        true
    } else {
        Log.d("NAVIGATION", "May not navigate: current destination is not the current fragment.")
        false
    }
}

fun Fragment.createFragmentMenu(
    @MenuRes menuLayout: Int,
    menuCallback: ((Menu) -> Unit)? = null,
    selectedItem: (menuItem: MenuItem) -> Unit
) {
    val menuHost = requireActivity()
    menuHost.addMenuProvider(
        object : MenuProvider {
            override fun onPrepareMenu(menu: Menu) {
                menuCallback?.invoke(menu)
                super.onPrepareMenu(menu)
            }

            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(menuLayout, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                selectedItem(menuItem)
                return true
            }
        },
        viewLifecycleOwner,
        Lifecycle.State.RESUMED
    )
}

internal inline fun Fragment.runIfFragmentIsAttached(block: () -> Unit) {
    context?.let { block() }
}

fun Fragment.nav(@IdRes id: Int?, directions: NavDirections, options: NavOptions? = null) {
    findNavController().nav(id, directions, options)
}

fun NavController.nav(@IdRes id: Int?, directions: NavDirections, navOptions: NavOptions? = null) {
    if (id == null || this.currentDestination?.id == id) {
        this.navigate(directions, navOptions)
    } else {
        Log.e("Fragment id ${this.currentDestination?.id}", "did not match expected $id")
    }
}
