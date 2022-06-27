package com.kl3jvi.animity.ui.widgets

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kl3jvi.animity.databinding.BottomSheetMediaListBinding


class MediaListDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: BottomSheetMediaListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomSheetMediaListBinding.inflate(inflater, container, false)
        return binding.root
    }
}
