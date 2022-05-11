package com.kl3jvi.animity.ui.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.viewbinding.ViewBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.ktx.Firebase
import com.kl3jvi.animity.ui.activities.main.MainActivity

abstract class BaseFragment<VM : ViewModel, VB : ViewBinding> : Fragment() {

    /* A property that is used to store the view model. */
    protected abstract val viewModel: VM

    /* A property that is used to store the view binding object. */
    protected lateinit var binding: VB

    /**
     * This function is called when the view model is observed.
     */
    abstract fun observeViewModel()

    /**
     * This function is used to initialize the views in the layout.
     */
    protected abstract fun initViews()

    /* Used to initialize the firebase analytics object. */
    lateinit var firebaseAnalytics: FirebaseAnalytics

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        /* Used to initialize the firebase analytics object. */
        firebaseAnalytics = Firebase.analytics
        /* Initializing the view binding object. */
        binding = getViewBinding()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModel()
        initViews()
    }


    /**
     * It returns a view binding object.
     */
    abstract fun getViewBinding(): VB

    /**
     * It checks if the device is connected to the internet.
     */
    fun isConnected(): Boolean = (activity as MainActivity).isConnected

    /**
     * It returns true if the user is logged in as a guest.
     */
    fun isGuestLogin(): Boolean = (activity as MainActivity).isGuestLogin

}