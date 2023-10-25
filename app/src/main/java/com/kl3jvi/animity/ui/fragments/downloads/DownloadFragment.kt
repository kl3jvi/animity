package com.kl3jvi.animity.ui.fragments.downloads

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kl3jvi.animity.R
import com.kl3jvi.animity.databinding.FragmentDownloadBinding
import com.kl3jvi.animity.downloads
import com.kl3jvi.animity.utils.BottomNavScrollListener
import com.kl3jvi.animity.utils.UiResult
import com.kl3jvi.animity.utils.collect
import com.kl3jvi.animity.utils.epoxy.setupBottomNavScrollListener
import com.kl3jvi.animity.utils.navigateSafe
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DownloadFragment : Fragment(R.layout.fragment_download) {
    private var binding: FragmentDownloadBinding? = null
    private val viewModel: DownloadViewModel by viewModels()
    private lateinit var listener: BottomNavScrollListener

    override fun onViewCreated(
        view: View,
        savedInstanceState: Bundle?,
    ) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDownloadBinding.bind(view)
        setupViews()
        listener = requireActivity() as BottomNavScrollListener
    }

    private fun setupViews() {
        binding?.downloadsList?.layoutManager = LinearLayoutManager(requireContext())
        binding?.downloadsList?.setOnLongClickListener {
            Log.e("TAG", "setupViews: ")
            true
        }

        collect(viewModel.listOfAnimes) {
            when (it) {
                is UiResult.Success -> {
                    binding?.downloadsList
                        ?.setupBottomNavScrollListener(listener)
                        ?.withModels {
                            it.data.forEach { anime ->
                                downloads {
                                    id(anime.id)
                                    anime(anime)
                                    clickListener { a ->
                                        a.navigateSafe(
                                            DownloadFragmentDirections.actionNavigationDownloadsToDownloadedEpisodesFragment(
                                                anime.id,
                                                anime.title
                                            ),
                                        )
                                    }
                                }
                            }
                        }
                }

                is UiResult.Error -> {}
                UiResult.Loading -> {}
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        binding = null
    }
}
