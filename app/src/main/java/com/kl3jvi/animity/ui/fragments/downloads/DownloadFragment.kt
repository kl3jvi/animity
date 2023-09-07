package com.kl3jvi.animity.ui.fragments.downloads

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentDownloadBinding
import com.kl3jvi.animity.downloads

class DownloadFragment : Fragment(R.layout.fragment_download) {

    private var binding: FragmentDownloadBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDownloadBinding.bind(view)
        setupViews()
    }

    private fun setupViews() {
        binding?.downloadsList?.withModels {
            downloads {
                id("download")
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
