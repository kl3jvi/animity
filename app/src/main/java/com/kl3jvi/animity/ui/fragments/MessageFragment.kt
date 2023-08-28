package com.kl3jvi.animity.ui.fragments

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentMessageBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MessageFragment : Fragment(R.layout.fragment_message) {
    //    private val viewModel: MessageViewModel by viewModels()
    private var binding: FragmentMessageBinding? = null
    private val args: MessageFragmentArgs by navArgs()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentMessageBinding.bind(view)
    }

}
