package com.kl3jvi.animity.ui.fragments.downloads

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.exoplayer2.offline.DownloadCursor
import com.google.android.exoplayer2.offline.DownloadManager
import com.kl3jvi.animity.databinding.FragmentDownloadsBinding
import com.kl3jvi.animity.ui.base.BaseFragment
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class DownloadsFragment : BaseFragment<DownloadsViewModel, FragmentDownloadsBinding>() {

    @Inject
    lateinit var downloadManager: DownloadManager
    override val viewModel: DownloadsViewModel by viewModels()
    override fun observeViewModel() {}
    override fun initViews() {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    private fun getDownloadedItems(): ArrayList<String> {
        val downloadedTracks = ArrayList<String>()
        val downloadCursor: DownloadCursor =
            downloadManager.downloadIndex.getDownloads()
        if (downloadCursor.moveToFirst()) {
            do {
                downloadCursor.download.bytesDownloaded
                val uri = downloadCursor.download.request.uri
                downloadedTracks.add(uri.toString())
            } while (downloadCursor.moveToNext())
        }
        return downloadedTracks
    }

    override fun getViewBinding(): FragmentDownloadsBinding =
        FragmentDownloadsBinding.inflate(layoutInflater)
}
