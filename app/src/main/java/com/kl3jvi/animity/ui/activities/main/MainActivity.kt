package com.kl3jvi.animity.ui.activities.main

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
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
import com.kl3jvi.animity.utils.hide
import com.kl3jvi.animity.utils.show
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

    var isGuestLogin: Boolean = true
    var isConnected: Boolean = true
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
    }

    fun hideBottomNavBar() {
        binding.navView.animate().translationY(binding.navView.height.toFloat()).duration = 500
        binding.navView.hide()
    }

    fun showBottomNavBar() {
        binding.navView.show()
        binding.navView.animate().translationY(0f).duration = 500
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration)
    }
}
