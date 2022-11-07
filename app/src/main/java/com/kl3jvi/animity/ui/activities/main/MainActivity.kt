package com.kl3jvi.animity.ui.activities.main

import android.os.Bundle
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.ActivityMainBinding
import com.kl3jvi.animity.utils.collectFlow
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        viewModel.initialise // just to initialise the viewmodel

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAnalytics = Firebase.analytics

        val navView: BottomNavigationView = binding.navView
        /* Getting the navigation controller from the navigation host fragment. */
        navController = findNavController(R.id.nav_host_fragment_activity_main)
        /* Used to set up the action bar with the navigation controller. */
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_favorites,
                R.id.navigation_explore,
                R.id.navigation_profile
            )
        )

        /* Setting up the action bar with the navigation controller. */
        setupActionBarWithNavController(navController, appBarConfiguration)
        /* Setting up the bottom navigation bar with the navigation controller. */
        /* Setting up the bottom navigation bar with the navigation controller. */
        navView.setupWithNavController(navController)

        bottomBarVisibility()
    }

    private fun hideBottomNavBar() {
        /* Hiding the bottom navigation bar. */
        binding.navView
            .animate()
            .translationY(binding.navView.height.toFloat())
            .setInterpolator(AccelerateInterpolator())
            .setInterpolator {
                if (it == 1F)
                    binding.navView.isVisible = false
                it
            }.duration = 400
    }

    private fun showBottomNavBar() {
        binding.navView
            .animate()
            .translationY(0f)
            .setInterpolator(DecelerateInterpolator())
            .setInterpolator {
                if (it == 0F)
                    binding.navView.isVisible = true
                it
            }.duration = 400

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }

    override fun onStart() {
        super.onStart()
        handleNetworkChanges()
    }

    private fun handleNetworkChanges() {
        collectFlow(viewModel.isConnectedToNetwork) { isConnected ->
            binding.wrapper.isVisible = isConnected
            binding.noInternetStatus.noInternet.isVisible = !isConnected
        }
    }


    /**
     * When the destination changes, if the destination is the details fragment or the review details
     * fragment, hide the bottom nav bar, otherwise show it.
     */
    private fun bottomBarVisibility() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if (destination.id == R.id.navigation_details || destination.id == R.id.reviewDetailsFragment) {
                hideBottomNavBar()
            } else {
                showBottomNavBar()
            }
        }
    }

}

