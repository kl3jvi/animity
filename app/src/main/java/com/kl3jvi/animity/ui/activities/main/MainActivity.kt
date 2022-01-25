package com.kl3jvi.animity.ui.activities.main

import android.os.Bundle
import android.view.View
import android.widget.PopupMenu
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import me.ibrahimsn.lib.SmoothBottomBar

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var firebaseAnalytics: FirebaseAnalytics
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        firebaseAnalytics = Firebase.analytics


        val navView: SmoothBottomBar = binding.navView
        val popupMenu = PopupMenu(this, null)
        popupMenu.inflate(R.menu.bottom_nav_menu)
        val menu = popupMenu.menu

        navController = findNavController(R.id.nav_host_fragment_activity_main)

        val appBarConfiguration =
            if (intent.extras != null) AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_favorites,
                    R.id.navigation_explore,
                )
            ) else AppBarConfiguration(
                setOf(
                    R.id.navigation_home,
                    R.id.navigation_favorites,
                    R.id.navigation_explore,
                    R.id.navigation_profile
                )
            )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(menu, navController)
    }

    fun hideBottomNavBar() {
        binding.navView.animate().translationY(binding.navView.height.toFloat()).duration = 300
        binding.navView.visibility = View.GONE
    }

    fun showBottomNavBar() {
        binding.navView.visibility = View.VISIBLE
        binding.navView.animate().translationY(0f).duration = 300
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
}
