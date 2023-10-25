package com.kl3jvi.animity.ui.activities.main

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.app.Dialog
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.kl3jvi.animity.R
import com.kl3jvi.animity.analytics.Analytics
import com.kl3jvi.animity.databinding.ActivityMainBinding
import com.kl3jvi.animity.settings.Settings
import com.kl3jvi.animity.ui.fragments.settings.SettingsFragment
import com.kl3jvi.animity.utils.BottomNavScrollListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Inject
import kotlin.math.pow

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity(), BottomNavScrollListener {
    private var isUIInitialized: Boolean = false
    lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    @Inject
    lateinit var settings: Settings

    @Inject
    lateinit var analytics: Analytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: NavigationBarView = binding.navView as NavigationBarView
        navController = findNavController(R.id.nav_host_fragment_activity_main)

        appBarConfiguration =
            AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_favorites,
                    R.id.navigation_downloads,
                    R.id.navigation_explore,
                    R.id.navigation_profile,
                ),
            )

        // Setting up the action bar with the navigation controller.
        setupActionBarWithNavController(
            navController,
            appBarConfiguration,
        )
        navView.setupWithNavController(navController)
        setBottomBarVisibility()
        askForPermission()

        settings.openCount += 1
        if (shouldShowDialog(settings.openCount) && !settings.maybeLater) {
            showDonationDialog()
        }
        isUIInitialized = true
    }

    private fun shouldShowDialog(openCount: Int): Boolean {
        val exponent = 1 + (openCount / 5) // starts with 1 after 5 openings, then increases
        val randomizedValue = (2.0.pow(exponent) % 10).toInt()
        Log.e("Calculated Val", (5 + randomizedValue).toString())
        return openCount == 5 + randomizedValue
    }

    private fun showDonationDialog() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.donate_dialog)

        // Set up toolbar
        val toolbar: Toolbar = dialog.findViewById(R.id.toolbar)
        toolbar.title = "Support Us"

        // Buttons
        val btnDonate: Button = dialog.findViewById(R.id.btnDonate)
        val btnLater: Button = dialog.findViewById(R.id.btnLater)

        btnDonate.setOnClickListener {
            // Handle donation action here
            openDonationPage()
            settings.maybeLater = true
            dialog.dismiss()
        }

        btnLater.setOnClickListener {
            dialog.dismiss()
            settings.maybeLater = true
        }
        val window = dialog.window
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt() // 90% of screen width
        window?.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)

        dialog.show()
    }

    private fun openDonationPage() {
        CustomTabsIntent.Builder()
            .build()
            .launchUrl(this, Uri.parse(SettingsFragment.KOFI_URL))
    }

    private val pushNotificationPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission(),
        ) { _ -> askForPermission() }

    private fun askForPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            pushNotificationPermissionLauncher.launch(android.Manifest.permission.POST_NOTIFICATIONS)
        }
    }

    // Hiding the bottom navigation bar.
    private fun hideBottomNavBar() {
        binding.navView.animate().translationY(binding.navView.height.toFloat())
            .setInterpolator(AccelerateInterpolator())
            .setListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        super.onAnimationEnd(animation)
                        binding.navView.isVisible = false
                    }
                },
            ).duration = ANIMATION_DURATION
    }

    // Showing the bottom navigation bar.
    private fun showBottomNavBar() {
        binding.navView.animate().translationY(0f).setInterpolator(DecelerateInterpolator())
            .setListener(
                object : AnimatorListenerAdapter() {
                    override fun onAnimationStart(animation: Animator) {
                        super.onAnimationStart(animation)
                        binding.navView.isVisible = true
                    }
                },
            ).duration = ANIMATION_DURATION
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }


    private fun setBottomBarVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id in
                arrayOf(
                    R.id.navigation_details,
                    R.id.reviewDetailsFragment,
                    R.id.settingsFragment,
                    R.id.scheduleFragment,
                    R.id.theirProfile,
                    R.id.navigation_downloaded_episodes
                )
            ) {
                hideBottomNavBar()
            } else {
                showBottomNavBar()
            }
        }
    }

    override fun onScrollDown() = hideBottomNavBar()
    override fun onScrollUp() = showBottomNavBar()

    companion object {
        const val ANIMATION_DURATION = 200L
    }
}
